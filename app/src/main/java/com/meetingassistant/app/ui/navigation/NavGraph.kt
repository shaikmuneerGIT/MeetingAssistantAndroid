package com.meetingassistant.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.meetingassistant.app.data.repository.MeetingRepository
import com.meetingassistant.app.services.LLMService
import com.meetingassistant.app.services.SpeechRecognitionService
import com.meetingassistant.app.services.TextToSpeechService
import com.meetingassistant.app.ui.screens.ChatScreen
import com.meetingassistant.app.ui.screens.HomeScreen
import com.meetingassistant.app.ui.screens.MeetingScreen
import com.meetingassistant.app.ui.screens.SettingsScreen
import com.meetingassistant.app.ui.screens.TranscriptScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Meeting : Screen("meeting")
    data object Chat : Screen("chat")
    data object Settings : Screen("settings")
    data object Transcript : Screen("transcript/{meetingId}") {
        fun createRoute(meetingId: String) = "transcript/$meetingId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    meetingRepository: MeetingRepository,
    speechService: SpeechRecognitionService,
    ttsService: TextToSpeechService,
    llmService: LLMService
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                meetingRepository = meetingRepository
            )
        }

        composable(Screen.Meeting.route) {
            MeetingScreen(
                navController = navController,
                meetingRepository = meetingRepository,
                speechService = speechService,
                ttsService = ttsService,
                llmService = llmService
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen(
                navController = navController,
                meetingRepository = meetingRepository,
                speechService = speechService,
                ttsService = ttsService,
                llmService = llmService
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                llmService = llmService,
                ttsService = ttsService
            )
        }

        composable(
            route = Screen.Transcript.route,
            arguments = listOf(navArgument("meetingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId") ?: ""
            TranscriptScreen(
                navController = navController,
                meetingRepository = meetingRepository,
                ttsService = ttsService,
                meetingId = meetingId
            )
        }
    }
}
