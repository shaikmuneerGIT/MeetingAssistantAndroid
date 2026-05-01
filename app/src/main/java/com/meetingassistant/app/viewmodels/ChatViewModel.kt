package com.meetingassistant.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingassistant.app.data.models.Message
import com.meetingassistant.app.data.models.MessageRole
import com.meetingassistant.app.data.repository.MeetingRepository
import com.meetingassistant.app.services.LLMService
import com.meetingassistant.app.services.SpeechRecognitionService
import com.meetingassistant.app.services.TextToSpeechService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val meetingRepository: MeetingRepository,
    private val speechService: SpeechRecognitionService,
    val ttsService: TextToSpeechService,
    private val llmService: LLMService
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isListeningForQuery = MutableStateFlow(false)
    val isListeningForQuery: StateFlow<Boolean> = _isListeningForQuery.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val isProcessing = llmService.isProcessing
    val isSpeaking = ttsService.isSpeaking

    private val autoSpeak: Boolean
        get() {
            val prefs = com.meetingassistant.app.MeetingAssistantApp.instance
                .getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
            return prefs.getBoolean("auto_speak_responses", true)
        }

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun sendMessage() {
        val text = _inputText.value.trim()
        if (text.isEmpty()) return

        val userMessage = Message(content = text, role = MessageRole.USER)
        _messages.value = _messages.value + userMessage
        _inputText.value = ""

        viewModelScope.launch {
            val context = buildMeetingContext()
            val result = llmService.sendMessage(
                messages = _messages.value,
                meetingContext = context
            )

            result.onSuccess { response ->
                val assistantMessage = Message(content = response, role = MessageRole.ASSISTANT)
                _messages.value = _messages.value + assistantMessage
                if (autoSpeak) {
                    ttsService.speak(response)
                }
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
        }
    }

    fun startVoiceQuery() {
        speechService.startListening(continuous = false) { text ->
            _inputText.value = text
        }
        _isListeningForQuery.value = true
    }

    fun stopVoiceQuery() {
        speechService.stopListening()
        _isListeningForQuery.value = false
    }

    fun submitVoiceQuery() {
        stopVoiceQuery()
        sendMessage()
    }

    fun speakMessage(message: Message) {
        ttsService.speak(message.content)
    }

    fun stopSpeaking() {
        ttsService.stop()
    }

    fun clearChat() {
        _messages.value = emptyList()
        ttsService.stop()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun buildMeetingContext(): String? {
        val meeting = meetingRepository.currentMeeting.value ?: return null

        val sb = StringBuilder()
        sb.appendLine("Meeting: ${meeting.title}")
        sb.appendLine("Duration: ${meeting.formattedDuration}")

        if (meeting.transcript.isNotEmpty()) {
            sb.appendLine("\nRecent Transcript:")
            meeting.transcript.takeLast(20).forEach { entry ->
                sb.appendLine("[${entry.formattedTimestamp}] ${entry.speaker ?: "Speaker"}: ${entry.text}")
            }
        }

        meeting.summary?.let {
            sb.appendLine("\nMeeting Summary:\n$it")
        }

        return sb.toString()
    }
}
