package com.meetingassistant.app.services

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SpeechRecognitionService(private val context: Context) {
    private var speechRecognizer: SpeechRecognizer? = null
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    // Tracks live in-progress speech (clears after each final result)
    private val _partialText = MutableStateFlow("")
    val partialText: StateFlow<String> = _partialText.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var onResultCallback: ((String) -> Unit)? = null
    private var continuousMode = false
    private var savedMusicVolume = 0
    private var savedNotifVolume = 0
    private var savedSystemVolume = 0

    // ---- Whisper integration ----
    private val whisperService = WhisperService(context)
    private val prefs get() = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private var usingWhisper = false
    private val forwardScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var forwardJobs = mutableListOf<Job>()

    val useWhisper: Boolean
        get() = prefs.getBoolean("use_whisper", false)

    val isAvailable: Boolean
        get() = if (useWhisper) true else SpeechRecognizer.isRecognitionAvailable(context)

    private fun muteBeep() {
        try {
            savedMusicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            savedNotifVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
            savedSystemVolume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0)
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0)
        } catch (_: Exception) {}
    }

    private fun unmuteBeep() {
        try {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, savedMusicVolume, 0)
            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, savedNotifVolume, 0)
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, savedSystemVolume, 0)
        } catch (_: Exception) {}
    }

    fun startListening(continuous: Boolean = true, onResult: (String) -> Unit) {
        if (useWhisper) {
            startWhisperListening(continuous, onResult)
            return
        }

        if (!isAvailable) {
            _error.value = "Speech recognition is not available on this device."
            return
        }

        onResultCallback = onResult
        continuousMode = continuous
        _error.value = null
        _partialText.value = ""
        usingWhisper = false

        muteBeep()

        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(createListener())
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            // Long silence timeouts so the recognizer keeps listening
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 15000L)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 15000L)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 60000L)
        }

        speechRecognizer?.startListening(intent)
        _isListening.value = true
    }

    fun stopListening() {
        if (usingWhisper) {
            stopWhisperListening()
            return
        }

        muteBeep()
        continuousMode = false
        speechRecognizer?.stopListening()
        speechRecognizer?.destroy()
        speechRecognizer = null
        _isListening.value = false
        _partialText.value = ""
        // Restore volume after a short delay to let any sound finish
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ unmuteBeep() }, 500)
    }

    fun destroy() {
        stopListening()
        whisperService.destroy()
        cancelForwardJobs()
    }

    // ---- Whisper delegation ----

    private fun startWhisperListening(continuous: Boolean, onResult: (String) -> Unit) {
        usingWhisper = true
        cancelForwardJobs()

        whisperService.startListening(continuous) { text ->
            _recognizedText.value = text
            onResult(text)
        }

        // Forward Whisper state flows to our own flows
        forwardJobs.add(forwardScope.launch {
            whisperService.isListening.collect { _isListening.value = it }
        })
        forwardJobs.add(forwardScope.launch {
            whisperService.partialText.collect { _partialText.value = it }
        })
        forwardJobs.add(forwardScope.launch {
            whisperService.error.collect { _error.value = it }
        })
    }

    private fun stopWhisperListening() {
        whisperService.stopListening()
        cancelForwardJobs()
        _isListening.value = false
        _partialText.value = ""
        usingWhisper = false
    }

    private fun cancelForwardJobs() {
        forwardJobs.forEach { it.cancel() }
        forwardJobs.clear()
    }

    // ---- Android SpeechRecognizer listener ----

    private fun createListener(): RecognitionListener {
        return object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _isListening.value = true
                // Restore volume after recognizer is ready (beep is done)
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ unmuteBeep() }, 300)
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(errorCode: Int) {
                // In continuous mode, silently restart on common non-fatal errors
                if (continuousMode) {
                    if (errorCode == SpeechRecognizer.ERROR_NO_MATCH ||
                        errorCode == SpeechRecognizer.ERROR_SPEECH_TIMEOUT ||
                        errorCode == SpeechRecognizer.ERROR_CLIENT) {
                        restartListening()
                        return
                    }
                }

                val message = when (errorCode) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No speech detected"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    else -> "Unknown error"
                }

                _error.value = message
                _isListening.value = false
                unmuteBeep()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: ""
                if (text.isNotEmpty()) {
                    _recognizedText.value = text
                    onResultCallback?.invoke(text)
                }
                _partialText.value = ""

                if (continuousMode) {
                    // Immediately restart - user hasn't manually paused
                    restartListening()
                } else {
                    _isListening.value = false
                    unmuteBeep()
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: ""
                if (text.isNotEmpty()) {
                    _partialText.value = text
                    _recognizedText.value = text
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    private fun restartListening() {
        speechRecognizer?.destroy()
        speechRecognizer = null

        val callback = onResultCallback ?: return
        // Fast restart - minimal gap so user doesn't notice
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            if (continuousMode) {
                startListening(continuous = true, onResult = callback)
            }
        }, 100)
    }
}
