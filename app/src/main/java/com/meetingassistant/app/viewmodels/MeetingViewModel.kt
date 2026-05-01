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

    private val _currentTranscript = MutableStateFlow("")
    val currentTranscript: StateFlow<String> = _currentTranscript.asStateFlow()

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

    val currentMeeting = meetingRepository.currentMeeting
    val isSpeaking = ttsService.isSpeaking
    val isProcessing = llmService.isProcessing

    private var timerJob: Job? = null
    private var lastProcessedText = ""
    private var lastAnalyzedTranscriptSize = 0

    fun startNewMeeting(title: String) {
        meetingRepository.startNewMeeting(title)
        startTimer()
        startRecording()
    }

    fun startRecording() {
        speechService.startListening(continuous = true) { text ->
            _currentTranscript.value = text
            handleRecognizedText(text)
        }
        _isRecording.value = true
    }

    fun stopRecording() {
        speechService.stopListening()
        _isRecording.value = false
        // Auto-analyze transcript when paused
        if (_autoAnalyzeOnPause.value) {
            analyzeOnPause()
        }
    }

    fun toggleRecording() {
        if (_isRecording.value) stopRecording() else {
            _pauseInsight.value = null  // Clear previous insight when resuming
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
     * Automatically triggered when recording is paused.
     * Sends recent transcript to LLM and speaks the analysis aloud.
     */
    private fun analyzeOnPause() {
        val meeting = meetingRepository.currentMeeting.value ?: return
        val transcript = meeting.transcript

        // Only analyze if there's new content since last analysis
        if (transcript.isEmpty() || transcript.size <= lastAnalyzedTranscriptSize) return

        viewModelScope.launch {
            _isAnalyzing.value = true
            _pauseInsight.value = null

            // Send only the new transcript entries since last analysis
            val newEntries = transcript.drop(lastAnalyzedTranscriptSize)
            val result = llmService.analyzeOnPause(newEntries)

            result.onSuccess { insight ->
                _pauseInsight.value = insight
                lastAnalyzedTranscriptSize = transcript.size

                // Auto-speak the insight
                val autoSpeak = com.meetingassistant.app.MeetingAssistantApp.instance
                    .getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
                    .getBoolean("auto_speak_responses", true)
                if (autoSpeak) {
                    ttsService.speak(insight)
                }
            }.onFailure { error ->
                _errorMessage.value = "Analysis failed: ${error.message}"
            }

            _isAnalyzing.value = false
        }
    }

    /** Manually trigger analysis of the full transcript */
    fun analyzeNow() {
        val meeting = meetingRepository.currentMeeting.value ?: return
        if (meeting.transcript.isEmpty()) {
            _errorMessage.value = "No transcript to analyze."
            return
        }
        viewModelScope.launch {
            _isAnalyzing.value = true
            val result = llmService.analyzeOnPause(meeting.transcript)
            result.onSuccess { insight ->
                _pauseInsight.value = insight
                lastAnalyzedTranscriptSize = meeting.transcript.size
                ttsService.speak(insight)
            }.onFailure { error ->
                _errorMessage.value = "Analysis failed: ${error.message}"
            }
            _isAnalyzing.value = false
        }
    }

    fun endMeeting() {
        stopRecording()
        stopTimer()
        meetingRepository.endMeeting()
    }

    fun generateSummary() {
        val meeting = meetingRepository.currentMeeting.value ?: return
        if (meeting.transcript.isEmpty()) {
            _errorMessage.value = "No transcript available to summarize."
            return
        }

        viewModelScope.launch {
            _isSummarizing.value = true
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

    private fun handleRecognizedText(text: String) {
        if (text != lastProcessedText && text.isNotBlank()) {
            meetingRepository.addTranscriptEntry(
                text = text,
                meetingTimer = _meetingTimer.value,
                speaker = "User"
            )
            lastProcessedText = text
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
        stopRecording()
        stopTimer()
    }
}
