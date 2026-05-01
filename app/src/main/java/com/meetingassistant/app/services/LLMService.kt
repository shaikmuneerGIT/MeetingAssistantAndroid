package com.meetingassistant.app.services

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.meetingassistant.app.data.models.Message
import com.meetingassistant.app.data.models.MessageRole
import com.meetingassistant.app.data.models.TranscriptEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class LLMService(private val context: Context) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val baseUrl = "https://api.openai.com/v1/chat/completions"

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var apiKey: String
        get() = prefs.getString("openai_api_key", "") ?: ""
        set(value) = prefs.edit().putString("openai_api_key", value).apply()

    var model: String
        get() = prefs.getString("llm_model", "gpt-4o-mini") ?: "gpt-4o-mini"
        set(value) = prefs.edit().putString("llm_model", value).apply()

    data class ChatRequest(
        val model: String,
        val messages: List<ChatMessage>,
        val temperature: Double = 0.7,
        @SerializedName("max_tokens") val maxTokens: Int = 2048
    )

    data class ChatMessage(
        val role: String,
        val content: String
    )

    data class ChatResponse(
        val choices: List<Choice>,
        val usage: Usage?
    )

    data class Choice(
        val message: ChatMessage,
        @SerializedName("finish_reason") val finishReason: String?
    )

    data class Usage(
        @SerializedName("prompt_tokens") val promptTokens: Int,
        @SerializedName("completion_tokens") val completionTokens: Int,
        @SerializedName("total_tokens") val totalTokens: Int
    )

    data class ErrorResponse(
        val error: ApiError
    )

    data class ApiError(
        val message: String,
        val type: String?
    )

    suspend fun sendMessage(
        messages: List<Message>,
        systemPrompt: String? = null,
        meetingContext: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isEmpty()) {
            return@withContext Result.failure(Exception("No API key configured. Please add your OpenAI API key in Settings."))
        }

        _isProcessing.value = true
        try {
            val chatMessages = mutableListOf<ChatMessage>()

            val systemContent = buildSystemPrompt(systemPrompt, meetingContext)
            chatMessages.add(ChatMessage("system", systemContent))

            messages.takeLast(20).forEach { msg ->
                chatMessages.add(ChatMessage(msg.role.value, msg.content))
            }

            val request = ChatRequest(
                model = model,
                messages = chatMessages
            )

            val json = gson.toJson(request)
            val body = json.toRequestBody("application/json".toMediaType())

            val httpRequest = Request.Builder()
                .url(baseUrl)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                val errorResponse = try {
                    gson.fromJson(responseBody, ErrorResponse::class.java)
                } catch (e: Exception) { null }

                val errorMsg = errorResponse?.error?.message ?: "HTTP error: ${response.code}"
                return@withContext Result.failure(Exception(errorMsg))
            }

            val chatResponse = gson.fromJson(responseBody, ChatResponse::class.java)
            val content = chatResponse.choices.firstOrNull()?.message?.content
                ?: return@withContext Result.failure(Exception("No response from AI"))

            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            _isProcessing.value = false
        }
    }

    suspend fun summarizeMeeting(transcript: List<TranscriptEntry>): Result<String> {
        val transcriptText = transcript.joinToString("\n") { entry ->
            "[${entry.formattedTimestamp}] ${entry.speaker ?: "Speaker"}: ${entry.text}"
        }

        val messages = listOf(
            Message(
                content = "Please provide a comprehensive summary of this meeting transcript. Include:\n1. Key discussion points\n2. Decisions made\n3. Action items\n4. Follow-up tasks\n\nTranscript:\n$transcriptText",
                role = MessageRole.USER
            )
        )

        return sendMessage(
            messages = messages,
            systemPrompt = "You are an expert meeting summarizer. Provide clear, structured summaries with actionable insights."
        )
    }

    suspend fun extractActionItems(transcript: List<TranscriptEntry>): Result<List<String>> {
        val transcriptText = transcript.joinToString("\n") { entry ->
            "[${entry.formattedTimestamp}] ${entry.speaker ?: "Speaker"}: ${entry.text}"
        }

        val messages = listOf(
            Message(
                content = "Extract all action items from this meeting transcript. Return each action item on a new line, prefixed with '- '. Include who is responsible if mentioned.\n\nTranscript:\n$transcriptText",
                role = MessageRole.USER
            )
        )

        val result = sendMessage(
            messages = messages,
            systemPrompt = "You are an expert at identifying action items from meeting transcripts."
        )

        return result.map { response ->
            response.lines()
                .map { it.trim() }
                .filter { it.startsWith("- ") || it.startsWith("* ") }
                .map { it.drop(2) }
        }
    }

    /**
     * Quick analysis triggered automatically when recording is paused.
     * Analyzes the recent transcript and returns key insights + suggested questions.
     */
    suspend fun analyzeOnPause(transcript: List<TranscriptEntry>): Result<String> {
        if (transcript.isEmpty()) {
            return Result.failure(Exception("No transcript to analyze."))
        }

        val transcriptText = transcript.joinToString("\n") { entry ->
            "[${entry.formattedTimestamp}] ${entry.speaker ?: "Speaker"}: ${entry.text}"
        }

        val messages = listOf(
            Message(
                content = """Recording was just paused. Analyze the latest meeting discussion and provide:

1. **Quick Summary** (2-3 sentences of what was just discussed)
2. **Key Points** (bullet points of important items)
3. **Questions to Consider** (2-3 follow-up questions that might be useful)
4. **Action Items Spotted** (any tasks or follow-ups mentioned)

Keep it concise and immediately useful. The user will hear this spoken aloud.

Recent Transcript:
$transcriptText""",
                role = MessageRole.USER
            )
        )

        return sendMessage(
            messages = messages,
            systemPrompt = """You are a real-time meeting assistant. When the user pauses their recording, 
you quickly analyze what was discussed and provide immediate, concise insights. 
Keep responses brief and spoken-word friendly since they will be read aloud via text-to-speech.
Use simple language, avoid markdown formatting symbols like ** or ##, and use natural pauses."""
        )
    }

    private fun buildSystemPrompt(customPrompt: String?, meetingContext: String?): String {
        var prompt = customPrompt ?: """
            You are an intelligent meeting assistant. Your role is to:
            1. Answer questions about the ongoing meeting discussion
            2. Provide summaries when asked
            3. Help identify action items and key decisions
            4. Offer relevant suggestions and insights
            5. Keep responses concise and professional
            
            Always be helpful, accurate, and focused on the meeting context.
        """.trimIndent()

        if (!meetingContext.isNullOrEmpty()) {
            prompt += "\n\nCurrent Meeting Context:\n$meetingContext"
        }

        return prompt
    }
}
