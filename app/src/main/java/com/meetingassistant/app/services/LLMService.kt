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
        @SerializedName("max_tokens") val maxTokens: Int = 512
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
        meetingContext: String? = null,
        temperature: Double = 0.7
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
                messages = chatMessages,
                temperature = temperature
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

    // ---- Speech correction & knowledge base ----

    /**
     * Common speech recognition errors mapped to correct terms.
     * Speech-to-text often mishears technical abbreviations.
     */
    private val speechCorrections = mapOf(
        "OAC" to "OIC",
        "O.A.C" to "OIC",
        "O A C" to "OIC",
        "oh I see" to "OIC",
        "oh icy" to "OIC",
        "VBC" to "VBCS",
        "V.B.C.S" to "VBCS",
        "V B C S" to "VBCS",
        "visual builder" to "VBCS",
        "E.R.P" to "ERP",
        "E R P" to "ERP",
        "HCN" to "HCM",
        "H.C.M" to "HCM",
        "SCN" to "SCM",
        "S.C.M" to "SCM",
        "PL sequel" to "PL/SQL",
        "P.L. SQL" to "PL/SQL",
        "BA reports" to "BI reports",
        "BA report" to "BI report",
        "FBDA" to "FBDI",
        "F.B.D.A" to "FBDI",
        "F B D A" to "FBDI"
    )

    /**
     * Map of known technology topics to their documentation files in assets.
     */
    private val topicDocs = mapOf(
        "OIC" to "docs/OIC-Complete-Guide.md",
        "VBCS" to "docs/VBCS-Complete-Guide.md",
        "ERP" to "docs/ERP-Complete-Guide.md"
    )

    /**
     * Corrects common speech recognition errors using the known terms map.
     */
    private fun correctSpeechErrors(text: String): String {
        var corrected = text
        for ((wrong, right) in speechCorrections) {
            corrected = corrected.replace(wrong, right, ignoreCase = true)
        }
        return corrected
    }

    /**
     * Detects which known topics are mentioned in the text.
     */
    private fun detectTopics(text: String): Set<String> {
        val upper = text.uppercase()
        return topicDocs.keys.filter { upper.contains(it) }.toSet()
    }

    /**
     * Loads documentation content from bundled assets for the given topics.
     * Truncates large docs to keep token usage reasonable.
     */
    private fun loadDocsForTopics(topics: Set<String>): String {
        if (topics.isEmpty()) return ""
        val sb = StringBuilder()
        for (topic in topics.take(2)) {
            val docPath = topicDocs[topic] ?: continue
            try {
                val content = context.assets.open(docPath).bufferedReader().readText()
                val truncated = if (content.length > 25000) {
                    content.take(25000) + "\n...[document truncated for length]"
                } else content
                sb.appendLine("\n\n=== Reference Knowledge Base: $topic ===\n")
                sb.appendLine(truncated)
            } catch (_: Exception) {
                // Doc file not found in assets, skip
            }
        }
        return sb.toString()
    }

    /**
     * Quick analysis triggered automatically when recording is paused.
     * Corrects speech errors, detects topics, loads relevant docs,
     * and provides precise answers from the knowledge base.
     *
     * @param latestTranscript The most recent transcript segment to focus on
     * @param fullTranscript   The full meeting transcript for context (optional)
     */
    suspend fun analyzeOnPause(
        latestTranscript: List<TranscriptEntry>,
        fullTranscript: List<TranscriptEntry>? = null
    ): Result<String> {
        if (latestTranscript.isEmpty()) {
            return Result.failure(Exception("No transcript to analyze."))
        }

        // Build and correct the transcript text
        val rawLatestText = latestTranscript.joinToString("\n") { entry ->
            "[${entry.formattedTimestamp}] ${entry.speaker ?: "Speaker"}: ${entry.text}"
        }
        val latestText = correctSpeechErrors(rawLatestText)

        // Build context from earlier transcript if available
        val contextBlock = if (!fullTranscript.isNullOrEmpty() && fullTranscript.size > latestTranscript.size) {
            val earlier = fullTranscript.dropLast(latestTranscript.size).takeLast(30)
            if (earlier.isNotEmpty()) {
                val contextText = earlier.joinToString("\n") { entry ->
                    "[${entry.formattedTimestamp}] ${entry.speaker ?: "Speaker"}: ${entry.text}"
                }
                "\n\nEarlier context from this meeting:\n${correctSpeechErrors(contextText)}"
            } else ""
        } else ""

        // Detect topics and load relevant documentation
        val allText = latestText + contextBlock
        val topics = detectTopics(allText)
        val docsContent = loadDocsForTopics(topics)

        val docsInstruction = if (docsContent.isNotEmpty()) {
            "\n\nIMPORTANT: Use the Reference Knowledge Base provided below to answer questions. Give answers based on this documentation.\n$docsContent"
        } else ""

        val messages = listOf(
            Message(
                content = """Here is what was just spoken in the meeting:

$latestText$contextBlock$docsInstruction

Instructions:
1. If any QUESTIONS were asked (e.g. "What is X?", "How to do Y?", "Explain Z"), ANSWER each question directly and accurately. If a Reference Knowledge Base is provided above, use it as your primary source.
2. If specific topics or problems were discussed, provide precise, relevant information for those exact topics.
3. Do NOT add information about topics that were NOT mentioned in the transcript.
4. Do NOT give generic advice or unrelated suggestions beyond what was asked.
5. Keep answers concise but complete - use short bullet points.
6. Only say "Not enough context" if the speech is completely unintelligible or garbled nonsense - NOT for short or simple questions.""",
                role = MessageRole.USER
            )
        )

        return sendMessage(
            messages = messages,
            systemPrompt = """You are a highly specialized Oracle Cloud technical assistant. You MUST ONLY answer questions related to Oracle ERP, Oracle Integration Cloud (OIC), and Visual Builder Cloud Service (VBCS).

Rules:
- FIRST correct any speech recognition errors. Common errors: OAC=OIC, VBC=VBCS, HCN=HCM, SCN=SCM, BA reports=BI reports, FBDA=FBDI.
- If a question is about ERP, OIC, or VBCS, ALWAYS answer it accurately using the Reference Knowledge Base first, then your general knowledge.
- If a question is NOT related to Oracle ERP, OIC, or VBCS, you MUST politely decline to answer and state that you are specialized only in Oracle ERP, OIC, and VBCS.
- Stay strictly on-topic.
- Be concise but informative. Use bullet points.""",
            temperature = 0.3
        )
    }

    private fun buildSystemPrompt(customPrompt: String?, meetingContext: String?): String {
        var prompt = customPrompt ?: """
            You are a specialized Oracle Cloud meeting assistant. Your role is exclusively focused on Oracle ERP, OIC, and VBCS.
            1. Answer questions ONLY if they relate to Oracle ERP, OIC, or VBCS.
            2. If asked about unrelated topics, politely state that you only support Oracle ERP, OIC, and VBCS.
            3. Provide summaries and action items focused on these technical areas.
            4. Keep responses concise, professional, and directly useful.
        """.trimIndent()

        if (!meetingContext.isNullOrEmpty()) {
            prompt += "\n\nCurrent Meeting Context:\n$meetingContext"
        }

        return prompt
    }
}
