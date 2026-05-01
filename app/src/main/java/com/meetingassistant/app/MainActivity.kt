package com.meetingassistant.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.meetingassistant.app.data.repository.MeetingRepository
import com.meetingassistant.app.services.LLMService
import com.meetingassistant.app.services.SpeechRecognitionService
import com.meetingassistant.app.services.TextToSpeechService
import com.meetingassistant.app.ui.navigation.NavGraph
import com.meetingassistant.app.ui.theme.MeetingAssistantTheme

class MainActivity : ComponentActivity() {
    private lateinit var meetingRepository: MeetingRepository
    private lateinit var speechService: SpeechRecognitionService
    private lateinit var ttsService: TextToSpeechService
    private lateinit var llmService: LLMService

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false
        if (!audioGranted) {
            // Permission denied - speech recognition won't work
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        meetingRepository = MeetingRepository(this)
        speechService = SpeechRecognitionService(this)
        ttsService = TextToSpeechService(this)
        llmService = LLMService(this)

        requestPermissions()

        setContent {
            MeetingAssistantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        meetingRepository = meetingRepository,
                        speechService = speechService,
                        ttsService = ttsService,
                        llmService = llmService
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechService.destroy()
        ttsService.destroy()
    }

    private fun requestPermissions() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET
            )
        )
    }
}
