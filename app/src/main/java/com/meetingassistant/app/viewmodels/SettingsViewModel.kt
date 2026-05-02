package com.meetingassistant.app.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.meetingassistant.app.services.LLMService
import com.meetingassistant.app.services.TextToSpeechService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val llmService: LLMService,
    private val ttsService: TextToSpeechService,
    private val context: Context
) : ViewModel() {

    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _apiKey = MutableStateFlow(llmService.apiKey)
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _selectedModel = MutableStateFlow(llmService.model)
    val selectedModel: StateFlow<String> = _selectedModel.asStateFlow()

    private val _useWhisper = MutableStateFlow(prefs.getBoolean("use_whisper", false))
    val useWhisper: StateFlow<Boolean> = _useWhisper.asStateFlow()

    private val _autoSpeakResponses = MutableStateFlow(prefs.getBoolean("auto_speak_responses", true))
    val autoSpeakResponses: StateFlow<Boolean> = _autoSpeakResponses.asStateFlow()

    private val _speechRate = MutableStateFlow(prefs.getFloat("speech_rate", 1.0f))
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _speechPitch = MutableStateFlow(prefs.getFloat("speech_pitch", 1.0f))
    val speechPitch: StateFlow<Float> = _speechPitch.asStateFlow()

    private val _showSaveConfirmation = MutableStateFlow(false)
    val showSaveConfirmation: StateFlow<Boolean> = _showSaveConfirmation.asStateFlow()

    val availableModels = listOf("gpt-4o-mini", "gpt-4o", "gpt-4-turbo", "gpt-3.5-turbo")

    val isApiKeyValid: Boolean
        get() = _apiKey.value.startsWith("sk-") && _apiKey.value.length > 20

    fun updateApiKey(key: String) { _apiKey.value = key }
    fun updateModel(model: String) { _selectedModel.value = model }
    fun updateUseWhisper(enabled: Boolean) { _useWhisper.value = enabled }
    fun updateAutoSpeak(enabled: Boolean) { _autoSpeakResponses.value = enabled }
    fun updateSpeechRate(rate: Float) { _speechRate.value = rate }
    fun updateSpeechPitch(pitch: Float) { _speechPitch.value = pitch }

    fun saveSettings() {
        llmService.apiKey = _apiKey.value
        llmService.model = _selectedModel.value
        ttsService.speechRate = _speechRate.value
        ttsService.pitch = _speechPitch.value

        prefs.edit()
            .putBoolean("use_whisper", _useWhisper.value)
            .putBoolean("auto_speak_responses", _autoSpeakResponses.value)
            .putFloat("speech_rate", _speechRate.value)
            .putFloat("speech_pitch", _speechPitch.value)
            .apply()

        _showSaveConfirmation.value = true
    }

    fun dismissSaveConfirmation() {
        _showSaveConfirmation.value = false
    }

    fun clearAllData() {
        prefs.edit().clear().apply()
        context.getSharedPreferences("meetings", Context.MODE_PRIVATE).edit().clear().apply()
        _apiKey.value = ""
        _selectedModel.value = "gpt-4o-mini"
        _useWhisper.value = false
        _autoSpeakResponses.value = true
        _speechRate.value = 1.0f
        _speechPitch.value = 1.0f
    }
}
