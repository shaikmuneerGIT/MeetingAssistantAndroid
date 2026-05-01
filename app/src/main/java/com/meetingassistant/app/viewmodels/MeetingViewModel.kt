package com.meetingassistant.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingassistant.app.data.repository.MeetingRepository
import com.meetingassistant.app.services.LLMService
import com.meetingassistant.app.services.SpeechRecognitionService
import com.meetingassistant.app.services.TextToSpeechService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MeetingViewModel(
    val meetingRepository: MeetingRepository,
    val speechService: SpeechRecognitionService,
    val ttsService: TextToSpeechService,
    val llmService: LLMService
) : ViewModel() {

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _meetingTimer = MutableStateFlow(0L)
    val meetingTimer: StateFlow<Long> = _meetingTimer.asStateFlow()

    private val _isSummarizing = MutableStateFlow(false)
    val isSummarizing: StateFlow<Boolean> = _isSummarizing.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _pauseInsight = MutableStateFlow<String?>(null)
    val pauseInsight: StateFlow<String?> = _pauseInsight.asStateFlow()

    private val _autoAnalyzeOnPause = MutableStateFlow(true)
    val autoAnalyzeOnPause: StateFlow<Boolean> = _autoAnalyzeOnPause.asStateFlow()

    /**
     * The live session text being built while recording.
     * This is NOT written to the transcript until the user manually pauses.
     */
    private val _sessionDisplayText = MutableStateFlow("")
    val sessionDisplayText: StateFlow<String> = _sessionDisplayText.asStateFlow()

    val currentMeeting = meetingRepository.currentMeeting
    val isSpeaking = ttsService.isSpeaking
    val isProcessing = llmService.isProcessing

    // Expose partial text from the speech service for live display
    val partialText = speechService.partialText

    private var timerJob: Job? = null
    private var lastProcessedText = ""

    // Session buffer: accumulates text until user manually pauses
    private val sessionText = StringBuilder()

    init {
        // Auto-start recording if there's an active meeting
        val meeting = meetingRepository.currentMeeting.value
        if (meeting != null && meeting.isActive) {
            startTimer()
            startRecording()
        }
    }

    fun startNewMeeting(title: String) {
        meetingRepository.startNewMeeting(title)
        startTimer()
        startRecording()
    }

    fun startRecording() {
        sessionText.clear()
        _sessionDisplayText.value = ""
        lastProcessedText = ""

        speechService.startListening(continuous = true) { text ->
            handleRecognizedText(text)
        }
        _isRecording.value = true
    }

    /**
     * Called ONLY when user manually taps pause.
     * Finalizes the session text into a transcript entry.
     */
    fun stopRecording() {
        speechService.stopListening()
        _isRecording.value = false

        // Commit the session text to the transcript
        commitSessionToTranscript()

        // Auto-analyze if API key is configured
        if (_autoAnalyzeOnPause.value && llmService.apiKey.isNotEmpty()) {
            analyzeOnPause()
        }
    }

    fun toggleRecording() {
        if (_isRecording.value) stopRecording() else {
            _pauseInsight.value = null
            startRecording()
        }
    }

    fun toggleAutoAnalyze() {
        _autoAnalyzeOnPause.value = !_autoAnalyzeOnPause.value
    }

    fun clearInsight() {
        _pauseInsight.value = null
    }

    /**
     * Writes the accumulated session text as a single transcript entry.
     */
    private fun commitSessionToTranscript() {
        val text = sessionText.toString().trim()
        if (text.isNotEmpty()) {
            meetingRepository.addTranscriptEntry(
                text = text,
                meetingTimer = _meetingTimer.value,
                speaker = "User"
            )
        }
        sessionText.clear()
        _sessionDisplayText.value = ""
    }

    /**
     * Automatically triggered when recording is paused.
     * Sends only the LATEST transcript entry to LLM for analysis.
     */
    private fun analyzeOnPause() {
        val meeting = meetingRepository.currentMeeting.value ?: return
        val transcript = meeting.transcript

        if (transcript.isEmpty()) return

        // Only analyze the last entry (what was just committed in this session)
        val latestEntry = listOf(transcript.last())

        viewModelScope.launch {
            _isAnalyzing.value = true
            _pauseInsight.value = null

            try {
                val result = llmService.analyzeOnPause(latestEntry)

                result.onSuccess { insight ->
                    _pauseInsight.value = insight
                }.onFailure { error ->
                    _errorMessage.value = "Analysis failed: ${error.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Analysis failed: ${e.message}"
            }

            _isAnalyzing.value = false
        }
    }

    /** Manually trigger analysis of the full transcript */
    fun analyzeNow() {
        if (llmService.apiKey.isEmpty()) {
            _errorMessage.value = "No API key configured. Please add your OpenAI API key in Settings."
            return
        }
        val meeting = meetingRepository.currentMeeting.value ?: return
        if (meeting.transcript.isEmpty()) {
            _errorMessage.value = "No transcript to analyze."
            return
        }
        viewModelScope.launch {
            _isAnalyzing.value = true
            try {
                val result = llmService.analyzeOnPause(meeting.transcript)
                result.onSuccess { insight ->
                    _pauseInsight.value = insight
                }.onFailure { error ->
                    _errorMessage.value = "Analysis failed: ${error.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Analysis failed: ${e.message}"
            }
            _isAnalyzing.value = false
        }
    }

    fun endMeeting() {
        // Stop recording without triggering auto-analyze
        speechService.stopListening()
        _isRecording.value = false

        // Commit any remaining session text
        commitSessionToTranscript()

        stopTimer()
        meetingRepository.endMeeting()
    }

    fun generateSummary() {
        if (llmService.apiKey.isEmpty()) {
            _errorMessage.value = "No API key configured. Please add your OpenAI API key in Settings."
            return
        }
        val meeting = meetingRepository.currentMeeting.value ?: return
        if (meeting.transcript.isEmpty()) {
            _errorMessage.value = "No transcript available to summarize."
            return
        }

        viewModelScope.launch {
            _isSummarizing.value = true
            try {
                val summaryResult = llmService.summarizeMeeting(meeting.transcript)
                summaryResult.onSuccess { summary ->
                    meetingRepository.updateMeetingSummary(summary)
                }.onFailure { error ->
                    _errorMessage.value = "Summary failed: ${error.message}"
                }

                val actionsResult = llmService.extractActionItems(meeting.transcript)
                actionsResult.onSuccess { items ->
                    meetingRepository.updateActionItems(items)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Summary failed: ${e.message}"
            }
            _isSummarizing.value = false
        }
    }

    fun speakSummary() {
        val summary = meetingRepository.currentMeeting.value?.summary ?: return
        ttsService.speak(summary)
    }

    fun stopSpeaking() {
        ttsService.stop()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun formatTimer(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return if (h > 0) String.format("%d:%02d:%02d", h, m, s)
        else String.format("%02d:%02d", m, s)
    }

    /**
     * Accumulates recognized text in the session buffer (NOT written to transcript yet).
     * The text only goes to the transcript when the user manually pauses.
     */
    private fun handleRecognizedText(text: String) {
        if (text != lastProcessedText && text.isNotBlank()) {
            lastProcessedText = text
            if (sessionText.isNotEmpty()) sessionText.append(" ")
            sessionText.append(text)
            _sessionDisplayText.value = sessionText.toString()
        }
    }

    private fun startTimer() {
        _meetingTimer.value = 0
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _meetingTimer.value += 1
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        speechService.stopListening()
        _isRecording.value = false
        stopTimer()
    }
}
