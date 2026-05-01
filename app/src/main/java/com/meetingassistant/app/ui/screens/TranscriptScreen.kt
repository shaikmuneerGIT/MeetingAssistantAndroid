package com.meetingassistant.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.meetingassistant.app.data.models.Meeting
import com.meetingassistant.app.data.repository.MeetingRepository
import com.meetingassistant.app.services.TextToSpeechService
import com.meetingassistant.app.ui.theme.Blue500
import com.meetingassistant.app.ui.theme.Orange500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranscriptScreen(
    navController: NavController,
    meetingRepository: MeetingRepository,
    ttsService: TextToSpeechService,
    meetingId: String
) {
    val meeting = remember { meetingRepository.getMeetingById(meetingId) }
    val isSpeaking by ttsService.isSpeaking.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    if (meeting == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Meeting not found")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(meeting.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val text = buildExportText(meeting)
                        val sendIntent = android.content.Intent().apply {
                            action = android.content.Intent.ACTION_SEND
                            putExtra(android.content.Intent.EXTRA_TEXT, text)
                            type = "text/plain"
                        }
                        context.startActivity(android.content.Intent.createChooser(sendIntent, "Share Meeting"))
                    }) {
                        Icon(Icons.Default.Share, "Share")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 },
                    text = { Text("Transcript") },
                    icon = { Icon(Icons.Default.FormatQuote, null, modifier = Modifier.size(18.dp)) }
                )
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 },
                    text = { Text("Summary") },
                    icon = { Icon(Icons.Default.Description, null, modifier = Modifier.size(18.dp)) }
                )
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 },
                    text = { Text("Actions") },
                    icon = { Icon(Icons.Default.Checklist, null, modifier = Modifier.size(18.dp)) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    0 -> TranscriptTab(meeting)
                    1 -> SummaryTab(meeting, isSpeaking, ttsService)
                    2 -> ActionsTab(meeting)
                }
            }
        }
    }
}

@Composable
private fun TranscriptTab(meeting: Meeting) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Duration: ${meeting.formattedDuration}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("${meeting.transcript.size} entries", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Spacer(modifier = Modifier.height(12.dp))

    if (meeting.transcript.isEmpty()) {
        EmptyState("No transcript available")
    } else {
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

@Composable
private fun SummaryTab(meeting: Meeting, isSpeaking: Boolean, ttsService: TextToSpeechService) {
    if (meeting.summary == null) {
        EmptyState("No summary generated yet")
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Meeting Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            IconButton(onClick = {
                if (isSpeaking) ttsService.stop() else ttsService.speak(meeting.summary!!)
            }) {
                Icon(
                    imageVector = if (isSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = null,
                    tint = Blue500
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(meeting.summary!!, style = MaterialTheme.typography.bodyMedium, lineHeight = MaterialTheme.typography.bodyMedium.lineHeight)
    }
}

@Composable
private fun ActionsTab(meeting: Meeting) {
    if (meeting.actionItems.isEmpty()) {
        EmptyState("No action items found")
    } else {
        Text("Action Items", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        meeting.actionItems.forEachIndexed { index, item ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    "${index + 1}.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Orange500,
                    modifier = Modifier.width(24.dp)
                )
                Text(item, style = MaterialTheme.typography.bodyMedium)
            }
            Divider(modifier = Modifier.padding(vertical = 2.dp))
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun buildExportText(meeting: Meeting): String {
    val sb = StringBuilder()
    sb.appendLine("Meeting: ${meeting.title}")
    sb.appendLine("Date: ${meeting.formattedDate}")
    sb.appendLine("Duration: ${meeting.formattedDuration}")
    sb.appendLine()
    sb.appendLine("--- TRANSCRIPT ---")
    meeting.transcript.forEach { entry ->
        sb.appendLine("[${entry.formattedTimestamp}] ${entry.speaker ?: "Speaker"}: ${entry.text}")
    }
    meeting.summary?.let {
        sb.appendLine()
        sb.appendLine("--- SUMMARY ---")
        sb.appendLine(it)
    }
    if (meeting.actionItems.isNotEmpty()) {
        sb.appendLine()
        sb.appendLine("--- ACTION ITEMS ---")
        meeting.actionItems.forEachIndexed { i, item ->
            sb.appendLine("${i + 1}. $item")
        }
    }
    return sb.toString()
}
