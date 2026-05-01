package com.meetingassistant.app.data.models

import java.util.UUID

data class Meeting(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: Long = System.currentTimeMillis(),
    var duration: Long = 0L,
    val transcript: MutableList<TranscriptEntry> = mutableListOf(),
    var summary: String? = null,
    val actionItems: MutableList<String> = mutableListOf(),
    var isActive: Boolean = false
) {
    val formattedDuration: String
        get() {
            val hours = duration / 3600
            val minutes = (duration % 3600) / 60
            val seconds = duration % 60
            return if (hours > 0) {
                String.format("%d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%d:%02d", minutes, seconds)
            }
        }

    val formattedDate: String
        get() {
            val sdf = java.text.SimpleDateFormat("MMM dd, yyyy hh:mm a", java.util.Locale.getDefault())
            return sdf.format(java.util.Date(date))
        }
}
