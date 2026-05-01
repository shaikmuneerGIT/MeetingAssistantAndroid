package com.meetingassistant.app.data.models

import java.util.UUID

data class TranscriptEntry(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val timestamp: Long,
    val speaker: String? = null,
    val confidence: Float = 1.0f
) {
    val formattedTimestamp: String
        get() {
            val minutes = (timestamp / 60).toInt()
            val seconds = (timestamp % 60).toInt()
            return String.format("%02d:%02d", minutes, seconds)
        }
}
