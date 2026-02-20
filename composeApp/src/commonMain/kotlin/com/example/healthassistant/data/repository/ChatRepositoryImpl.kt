package com.example.healthassistant.data.repository

import ChatReportWrapperDto
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.core.utils.DateTime
import com.example.healthassistant.data.local.chat.ChatLocalDataSource
import com.example.healthassistant.data.local.profile.ProfileLocalDataSource
import com.example.healthassistant.data.local.report.ReportLocalDataSource
import com.example.healthassistant.data.remote.assessment.dto.ReportResponseDto
import com.example.healthassistant.data.remote.chat.ChatApi
import com.example.healthassistant.data.remote.chat.dto.ChatHistoryDto
import com.example.healthassistant.data.remote.chat.dto.ChatMessageRequestDto
import com.example.healthassistant.data.remote.chat.dto.ChatStartRequestDto
import com.example.healthassistant.data.remote.chat.dto.ProfileDataDto
import com.example.healthassistant.data.remote.chat.dto.ProfileItemDto
import com.example.healthassistant.data.remote.chat.mapper.toDomain
import com.example.healthassistant.data.remote.chat.mapper.toReportResponseDto
import com.example.healthassistant.domain.model.chat.ChatMessage
import com.example.healthassistant.domain.model.chat.Role
import com.example.healthassistant.domain.repository.ChatRepository
import kotlinx.serialization.json.Json

class ChatRepositoryImpl(
    private val api: ChatApi,
    private val local: ChatLocalDataSource,
    private val profileLocal: ProfileLocalDataSource,
    private val reportLocal: ReportLocalDataSource
) : ChatRepository {

    override suspend fun startChat(
        currentReportId: String?
    ): ChatMessage {

        AppLogger.d("CHAT_REPO", "===== START CHAT PROCESS BEGIN =====")
        AppLogger.d("CHAT_REPO", "Current Report ID → $currentReportId")

        // 1️⃣ Fetch profile answers
        val profileAnswers = profileLocal.getAll()
        AppLogger.d("CHAT_REPO", "Profile answers fetched → count=${profileAnswers.size}")

        // 2️⃣ Fetch reports
        val reports = reportLocal.getAll()
        AppLogger.d("CHAT_REPO", "Reports fetched → count=${reports.size}")

        // 3️⃣ Map reports
        val reportWrappers = reports.map { report ->

            val reportDto = report.toReportResponseDto()

            ChatReportWrapperDto(
                report_id = reportDto.report_id,
                generated_at = reportDto.generated_at,
                is_main = report.reportId == currentReportId,
                report_data = reportDto
            )
        }

        // 4️⃣ Build request
        val request = ChatStartRequestDto(
            profile_data = profileAnswers.map {
                ProfileDataDto(
                    question = it.questionText,
                    answer = it.answerText
                )
            },
            reports = reportWrappers
        )

        AppLogger.logJson("CHAT_REPO", "START REQUEST BODY", request)

        // 5️⃣ Call backend
        AppLogger.d("CHAT_REPO", "Calling API → /chat/start")
        val response = api.startChat(request)

        AppLogger.d("CHAT_REPO", "API RESPONSE → sessionId=${response.session_id}")
        AppLogger.d("CHAT_REPO", "Greeting → ${response.message}")

        val assistantMessage = response.toDomain()

        // 6️⃣ Store locally
        local.insert(assistantMessage)
        AppLogger.d("CHAT_REPO", "Greeting stored in local DB")

        AppLogger.d("CHAT_REPO", "===== START CHAT PROCESS END =====")

        return assistantMessage
    }

    override suspend fun sendMessage(
        sessionId: String,
        userMessage: String
    ): ChatMessage {

        AppLogger.d("CHAT_REPO", "===== SEND MESSAGE BEGIN =====")
        AppLogger.d("CHAT_REPO", "Session ID → $sessionId")
        AppLogger.d("CHAT_REPO", "User message → $userMessage")

        val user = ChatMessage(
            id = sessionId + "_user_" + DateTime.getCurrentTimeMillis(),
            sessionId = sessionId,
            role = Role.USER,
            content = userMessage,
            timestamp = DateTime.getCurrentTimeMillis()
        )

        local.insert(user)

        val history = local.getMessages(sessionId)


        val request = ChatMessageRequestDto(
            session_id = sessionId,
            history = history.map {
                ChatHistoryDto(
                    role = it.role.name.lowercase(),
                    content = it.content
                )
            }
        )
        AppLogger.d("CHAT_REPO", "History size → ${history.size}")
        AppLogger.logJson("CHAT_REPO", "MESSAGE REQUEST BODY", request)

        val response = api.sendMessage(request)
        AppLogger.d("CHAT_REPO", "Assistant reply received")
        AppLogger.d("CHAT_REPO", "Reply content → ${response.message}")

        val assistantMessage = response.toDomain(sessionId)

        local.insert(assistantMessage)
        AppLogger.d("CHAT_REPO", "===== SEND MESSAGE END =====")

        return assistantMessage
    }

    override suspend fun getMessages(sessionId: String): List<ChatMessage> {
        return local.getMessages(sessionId)
    }

    override suspend fun endChat(sessionId: String) {
        try {
            api.endChat(sessionId)
        } catch (_: Exception) {
        }

        local.clear(sessionId)
    }
}