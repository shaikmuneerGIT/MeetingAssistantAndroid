package com.meetingassistant.app.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.meetingassistant.app.data.models.Meeting
import com.meetingassistant.app.data.models.TranscriptEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MeetingRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("meetings", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _meetings = MutableStateFlow<List<Meeting>>(emptyList())
    val meetings: StateFlow<List<Meeting>> = _meetings.asStateFlow()

    private val _currentMeeting = MutableStateFlow<Meeting?>(null)
    val currentMeeting: StateFlow<Meeting?> = _currentMeeting.asStateFlow()

    init {
        loadMeetings()
    }

    fun startNewMeeting(title: String): Meeting {
        val meeting = Meeting(title = title, isActive = true)
        _currentMeeting.value = meeting
        return meeting
    }

    fun endMeeting() {
        val meeting = _currentMeeting.value ?: return
        meeting.isActive = false
        
        val currentList = _meetings.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == meeting.id }
        if (index >= 0) {
            currentList[index] = meeting
        } else {
            currentList.add(0, meeting)
        }
        _meetings.value = currentList
        saveMeetings()
        _currentMeeting.value = null
    }

    fun addTranscriptEntry(text: String, meetingTimer: Long, speaker: String? = null, confidence: Float = 1.0f) {
        val meeting = _currentMeeting.value ?: return
        val entry = TranscriptEntry(
            text = text,
            timestamp = meetingTimer,
            speaker = speaker,
            confidence = confidence
        )
        meeting.transcript.add(entry)
        _currentMeeting.value = meeting.copy(transcript = meeting.transcript)
    }

    /**
     * Updates the text of the last transcript entry in the current meeting.
     * Used to accumulate text within a single recording session.
     */
    fun updateLastTranscriptEntry(text: String) {
        val meeting = _currentMeeting.value ?: return
        if (meeting.transcript.isEmpty()) return
        val lastEntry = meeting.transcript.last()
        meeting.transcript[meeting.transcript.lastIndex] = lastEntry.copy(text = text)
        _currentMeeting.value = meeting.copy(transcript = meeting.transcript)
    }

    fun updateMeetingSummary(summary: String) {
        val meeting = _currentMeeting.value ?: return
        meeting.summary = summary
        _currentMeeting.value = meeting.copy(summary = summary)
        
        val currentList = _meetings.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == meeting.id }
        if (index >= 0) {
            currentList[index] = meeting
        }
        _meetings.value = currentList
        saveMeetings()
    }

    fun updateActionItems(items: List<String>) {
        val meeting = _currentMeeting.value ?: return
        meeting.actionItems.clear()
        meeting.actionItems.addAll(items)
        _currentMeeting.value = meeting.copy(actionItems = meeting.actionItems)

        val currentList = _meetings.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == meeting.id }
        if (index >= 0) {
            currentList[index] = meeting
        }
        _meetings.value = currentList
        saveMeetings()
    }

    fun deleteMeeting(meeting: Meeting) {
        val currentList = _meetings.value.toMutableList()
        currentList.removeAll { it.id == meeting.id }
        _meetings.value = currentList
        saveMeetings()
    }

    fun clearAllMeetings() {
        _meetings.value = emptyList()
        _currentMeeting.value = null
        saveMeetings()
    }

    fun getMeetingById(id: String): Meeting? {
        return _meetings.value.find { it.id == id }
    }

    private fun saveMeetings() {
        val json = gson.toJson(_meetings.value)
        prefs.edit().putString("saved_meetings", json).apply()
    }

    private fun loadMeetings() {
        val json = prefs.getString("saved_meetings", null)
        if (json != null) {
            val type = object : TypeToken<List<Meeting>>() {}.type
            val loaded: List<Meeting> = gson.fromJson(json, type)
            _meetings.value = loaded
        }
    }
}
