package com.meetingassistant.app.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext
import kotlin.math.sqrt

/**
 * Speech-to-text service using OpenAI Whisper API.
 *
 * Records audio via AudioRecord, writes WAV chunks, and sends them to
 * the Whisper transcription endpoint for high-accuracy results.
 */
class WhisperService(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private val prefs get() = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val apiKey: String
        get() = prefs.getString("openai_api_key", "") ?: ""

    // Audio recording parameters – 16 kHz mono 16-bit PCM
    private val sampleRate = 16_000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioEncoding = AudioFormat.ENCODING_PCM_16BIT
    private val minBufferSize =
        AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioEncoding)

    private var audioRecord: AudioRecord? = null
    private var scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // ---- Public state (mirrors SpeechRecognitionService API) ----

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    private val _partialText = MutableStateFlow("")
    val partialText: StateFlow<String> = _partialText.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var onResultCallback: ((String) -> Unit)? = null
    private var continuousMode = false

    /** Job that completes when the remaining audio after stop has been transcribed. */
    var pendingFlushJob: Job? = null
        private set

    // Accumulated PCM data for the current chunk
    private val audioChunks = mutableListOf<ByteArray>()

    // Flush every 10 s in continuous mode
    private val flushIntervalMs = 10_000L

    // Silence detection
    private val silenceRmsThreshold = 500
    private val silenceTriggerMs = 2_000L

    // ---- Whisper API response models ----

    private data class WhisperResponse(val text: String)
    private data class WhisperErrorResponse(val error: WhisperErrorDetail)
    private data class WhisperErrorDetail(val message: String, val type: String?)

    // ---- Public API ----

    fun startListening(continuous: Boolean = true, onResult: (String) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            _error.value = "Microphone permission not granted."
            return
        }
        if (apiKey.isEmpty()) {
            _error.value = "No OpenAI API key configured. Please add it in Settings."
            return
        }

        onResultCallback = onResult
        continuousMode = continuous
        _error.value = null
        _partialText.value = ""
        _recognizedText.value = ""
        audioChunks.clear()

        try {
            val bufferSize = maxOf(minBufferSize * 2, sampleRate * 2) // at least 1 s buffer
            @Suppress("MissingPermission")
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioEncoding,
                bufferSize
            )

            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                _error.value = "Failed to initialise audio recorder."
                return
            }

            audioRecord?.startRecording()
            _isListening.value = true

            scope.launch { recordLoop() }
        } catch (e: Exception) {
            _error.value = "Recording start failed: ${e.message}"
        }
    }

    fun stopListening() {
        val wasListening = _isListening.value
        continuousMode = false
        _isListening.value = false
        _partialText.value = ""

        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null

        // Transcribe whatever is left in the buffer
        if (wasListening && audioChunks.isNotEmpty()) {
            val remaining = audioChunks.toList()
            audioChunks.clear()
            pendingFlushJob = scope.launch { transcribeAndDeliver(remaining) }
        } else {
            pendingFlushJob = null
        }
    }

    fun destroy() {
        stopListening()
        scope.cancel()
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    // ---- Recording loop ----

    private suspend fun recordLoop() {
        val readBuffer = ByteArray(minBufferSize)
        var lastFlushTime = System.currentTimeMillis()
        var silenceStart = 0L
        var inSilence = false

        try {
            while (coroutineContext[kotlinx.coroutines.Job]?.isActive == true && _isListening.value) {
                val bytesRead = audioRecord?.read(readBuffer, 0, readBuffer.size) ?: -1
                if (bytesRead <= 0) continue

                synchronized(audioChunks) { audioChunks.add(readBuffer.copyOf(bytesRead)) }

                val rms = calculateRms(readBuffer, bytesRead)
                val now = System.currentTimeMillis()

                // Silence tracking
                if (rms < silenceRmsThreshold) {
                    if (!inSilence) { silenceStart = now; inSilence = true }
                } else {
                    inSilence = false
                    withContext(Dispatchers.Main) { _partialText.value = "..." }
                }

                val elapsed = now - lastFlushTime
                val silenceDur = if (inSilence) now - silenceStart else 0L

                val shouldFlush = if (continuousMode) {
                    elapsed >= flushIntervalMs ||
                        (silenceDur >= silenceTriggerMs && elapsed >= 2_000L)
                } else {
                    silenceDur >= silenceTriggerMs && elapsed >= 1_000L
                }

                if (shouldFlush) {
                    val data: List<ByteArray>
                    synchronized(audioChunks) {
                        data = audioChunks.toList()
                        audioChunks.clear()
                    }
                    if (data.isNotEmpty()) {
                        transcribeAndDeliver(data)
                        lastFlushTime = System.currentTimeMillis()
                    }

                    if (!continuousMode) {
                        withContext(Dispatchers.Main) { _isListening.value = false }
                        break
                    }
                }
            }
        } catch (_: CancellationException) {
            // Normal stop
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _error.value = "Recording error: ${e.message}"
                _isListening.value = false
            }
        }
    }

    // ---- Transcription ----

    private suspend fun transcribeAndDeliver(pcmData: List<ByteArray>) {
        try {
            val wavFile = writeWav(pcmData)
            val text = callWhisperApi(wavFile)
            wavFile.delete()

            if (text.isNotBlank()) {
                withContext(Dispatchers.Main) {
                    _recognizedText.value = text
                    _partialText.value = ""
                    onResultCallback?.invoke(text)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _error.value = "Transcription failed: ${e.message}"
            }
        }
    }

    private fun callWhisperApi(audioFile: File): String {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", audioFile.name,
                audioFile.asRequestBody("audio/wav".toMediaType())
            )
            .addFormDataPart("model", "whisper-1")
            .addFormDataPart("response_format", "json")
            .addFormDataPart("language", "en")
            .build()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/audio/transcriptions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        if (!response.isSuccessful) {
            val err = try {
                gson.fromJson(responseBody, WhisperErrorResponse::class.java)
            } catch (_: Exception) { null }
            throw Exception(err?.error?.message ?: "Whisper API error ${response.code}")
        }

        return gson.fromJson(responseBody, WhisperResponse::class.java).text.trim()
    }

    // ---- WAV helpers ----

    private fun writeWav(pcmData: List<ByteArray>): File {
        val file = File(context.cacheDir, "whisper_${System.currentTimeMillis()}.wav")
        val dataSize = pcmData.sumOf { it.size }

        FileOutputStream(file).use { out ->
            // RIFF header
            out.write("RIFF".toByteArray())
            out.write(toLittleEndianInt(36 + dataSize))
            out.write("WAVE".toByteArray())
            // fmt sub-chunk
            out.write("fmt ".toByteArray())
            out.write(toLittleEndianInt(16))          // sub-chunk size
            out.write(toLittleEndianShort(1))         // PCM
            out.write(toLittleEndianShort(1))         // mono
            out.write(toLittleEndianInt(sampleRate))   // sample rate
            out.write(toLittleEndianInt(sampleRate * 2)) // byte rate
            out.write(toLittleEndianShort(2))         // block align
            out.write(toLittleEndianShort(16))        // bits per sample
            // data sub-chunk
            out.write("data".toByteArray())
            out.write(toLittleEndianInt(dataSize))
            pcmData.forEach { out.write(it) }
        }
        return file
    }

    private fun toLittleEndianInt(v: Int) = byteArrayOf(
        (v and 0xFF).toByte(),
        ((v shr 8) and 0xFF).toByte(),
        ((v shr 16) and 0xFF).toByte(),
        ((v shr 24) and 0xFF).toByte()
    )

    private fun toLittleEndianShort(v: Int) = byteArrayOf(
        (v and 0xFF).toByte(),
        ((v shr 8) and 0xFF).toByte()
    )

    private fun calculateRms(buffer: ByteArray, length: Int): Int {
        var sum = 0L
        val samples = length / 2
        for (i in 0 until samples) {
            val sample = (buffer[i * 2 + 1].toInt() shl 8) or (buffer[i * 2].toInt() and 0xFF)
            sum += sample.toLong() * sample.toLong()
        }
        return if (samples > 0) sqrt(sum.toDouble() / samples).toInt() else 0
    }
}
