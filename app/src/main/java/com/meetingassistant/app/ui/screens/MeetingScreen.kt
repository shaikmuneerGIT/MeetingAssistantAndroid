package com.meetingassistant.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.meetingassistant.app.data.repository.MeetingRepository
import com.meetingassistant.app.services.LLMService
import com.meetingassistant.app.services.SpeechRecognitionService
import com.meetingassistant.app.services.TextToSpeechService
import com.meetingassistant.app.ui.components.VoiceButton
import com.meetingassistant.app.ui.navigation.Screen
import com.meetingassistant.app.ui.theme.Blue500
import com.meetingassistant.app.ui.theme.Green500
import com.meetingassistant.app.ui.theme.Orange500
import com.meetingassistant.app.ui.theme.Purple500
import com.meetingassistant.app.ui.theme.Red500
import com.meetingassistant.app.viewmodels.MeetingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingScreen(
    navController: NavController,
    meetingRepository: MeetingRepository,
    speechService: SpeechRecognitionService,
    ttsService: TextToSpeechService,
    llmService: LLMService
) {
    val viewModel = remember {
        MeetingViewModel(meetingRepository, speechService, ttsService, llmService)
    }

    val currentMeeting by viewModel.currentMeeting.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()
    val currentTranscript by viewModel.currentTranscript.collectAsState()
    val meetingTimer by viewModel.meetingTimer.collectAsState()
    val isSummarizing by viewModel.isSummarizing.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val pauseInsight by viewModel.pauseInsight.collectAsState()
    val autoAnalyze by viewModel.autoAnalyzeOnPause.collectAsState()

    var showEndDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentMeeting?.title ?: "Meeting") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, "Menu")
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("Generate Summary") },
                                leadingIcon = { Icon(Icons.Default.Description, null) },
                                onClick = {
                                    showMenu = false
                                    viewModel.generateSummary()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Analyze Now") },
                                leadingIcon = { Icon(Icons.Default.AutoAwesome, null, tint = Purple500) },
                                onClick = {
                                    showMenu = false
                                    viewModel.analyzeNow()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(if (autoAnalyze) "Auto-Analyze: ON" else "Auto-Analyze: OFF") },
                                leadingIcon = {
                                    Icon(
                                        if (autoAnalyze) Icons.Default.ToggleOn else Icons.Default.ToggleOff,
                                        null,
                                        tint = if (autoAnalyze) Green500 else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = {
                                    viewModel.toggleAutoAnalyze()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Ask AI") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Screen.Chat.route)
                                }
                            )
                            Divider()
                            DropdownMenuItem(
                                text = { Text("End Meeting", color = Red500) },
                                leadingIcon = { Icon(Icons.Default.Stop, null, tint = Red500) },
                                onClick = {
                                    showMenu = false
                                    showEndDialog = true
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { viewModel.generateSummary() },
                        enabled = !isSummarizing
                    ) {
                        Icon(Icons.Default.Description, "Summary", tint = Blue500)
                    }
                    Text("Summary", style = MaterialTheme.typography.labelSmall)
                }

                VoiceButton(isListening = isRecording, onClick = { viewModel.toggleRecording() })

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { showEndDialog = true }) {
                        Icon(Icons.Default.Stop, "End", tint = Red500)
                    }
                    Text("End", style = MaterialTheme.typography.labelSmall, color = Red500)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isRecording) Red500 else if (isAnalyzing) Purple500 else MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            isRecording -> "Recording"
                            isAnalyzing -> "Analyzing..."
                            else -> "Paused"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    if (autoAnalyze && !isRecording) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("(Auto-AI)", style = MaterialTheme.typography.labelSmall, color = Purple500)
                    }
                }
                Text(
                    text = viewModel.formatTimer(meetingTimer),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                if (isSummarizing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Spacer(modifier = Modifier.size(20.dp))
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text("Live Transcript", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(16.dp)
                ) {
                    Text(
                        text = currentTranscript.ifEmpty { "Listening for speech..." },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (currentTranscript.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // --- AI Insight Panel (shown when paused & analyzed) ---
            if (isAnalyzing) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Purple500)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Analyzing transcript with AI...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Purple500,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            pauseInsight?.let { insight ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Purple500, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("AI Insight", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Purple500)
                        }
                        Row {
                            IconButton(onClick = {
                                if (isSpeaking) viewModel.stopSpeaking() else ttsService.speak(insight)
                            }, modifier = Modifier.size(32.dp)) {
                                Icon(
                                    if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                    null, tint = Blue500, modifier = Modifier.size(18.dp)
                                )
                            }
                            TextButton(onClick = { viewModel.clearInsight() }, modifier = Modifier.height(32.dp)) {
                                Text("Dismiss", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Purple500.copy(alpha = 0.07f))
                            .padding(14.dp)
                    ) {
                        Text(insight, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            // --- End AI Insight Panel ---

            currentMeeting?.let { meeting ->
                if (meeting.transcript.isNotEmpty()) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Transcript History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text("${meeting.transcript.size} entries", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        meeting.transcript.forEach { entry ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = entry.formattedTimestamp,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.width(44.dp)
                                )
                                Column {
                                    entry.speaker?.let {
                                        Text(it, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Blue500)
                                    }
                                    Text(entry.text, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            Divider(modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                }

                meeting.summary?.let { summary ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("AI Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            IconButton(onClick = {
                                if (isSpeaking) viewModel.stopSpeaking() else viewModel.speakSummary()
                            }) {
                                Icon(
                                    imageVector = if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = Blue500
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Blue500.copy(alpha = 0.05f))
                                .padding(16.dp)
                        ) {
                            Text(summary, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                if (meeting.actionItems.isNotEmpty()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Action Items", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        meeting.actionItems.forEach { item ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Icon(Icons.Default.CheckCircle, null, tint = Orange500, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(item, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (showEndDialog) {
        AlertDialog(
            onDismissRequest = { showEndDialog = false },
            title = { Text("End Meeting?") },
            text = { Text("This will stop recording and save the meeting.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.endMeeting()
                        showEndDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500)
                ) { Text("End Meeting") }
            },
            dismissButton = {
                TextButton(onClick = { showEndDialog = false }) { Text("Cancel") }
            }
        )
    }

    errorMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(msg) },
            confirmButton = { TextButton(onClick = { viewModel.clearError() }) { Text("OK") } }
        )
    }
}
