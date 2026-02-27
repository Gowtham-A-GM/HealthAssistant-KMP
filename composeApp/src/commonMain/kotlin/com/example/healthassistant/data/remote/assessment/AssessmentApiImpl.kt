package com.example.healthassistant.data.remote.assessment

import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.data.remote.assessment.dto.ReportDto
import com.example.healthassistant.data.remote.assessment.dto.StartAssessmentResponseDto
import com.example.healthassistant.data.remote.assessment.dto.SubmitAnswerRequestDto
import com.example.healthassistant.data.remote.assessment.dto.SubmitAnswerResponseDto
import com.example.healthassistant.data.remote.assessment.dto.SubmitReportRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AssessmentApiImpl(
    private val client: HttpClient,
    private val baseUrl: String
) : AssessmentApi {

    override suspend fun startAssessment(): StartAssessmentResponseDto {

        AppLogger.d("API", "GET /assessment/start")

        val response =
            client.get("$baseUrl/assessment/start").body<StartAssessmentResponseDto>()

        AppLogger.d(
            "API",
            "Response received → session_id=${response.session_id}"
        )

        return response
    }

    override suspend fun submitAnswer(
        request: SubmitAnswerRequestDto
    ): SubmitAnswerResponseDto {

        val response =
            client.post("$baseUrl/assessment/answer") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<SubmitAnswerResponseDto>()

        AppLogger.d("API", "Answer status → ${response.status}")

        return response
    }

    override suspend fun submitReport(
        request: SubmitReportRequestDto
    ): ReportDto {

        AppLogger.d("API", "POST /assessment/report → session_id=${request.session_id}")

        return client.post("$baseUrl/assessment/report") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun endSession(sessionId: String) {

        AppLogger.d("API", "POST /assessment/end → $sessionId")

        client.post("$baseUrl/assessment/end") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("session_id" to sessionId))
        }
    }

    override suspend fun getUserReports(): List<ReportDto> {

        AppLogger.d("API", "GET /user/reports")

        return client.get("$baseUrl/user/reports") {
            contentType(ContentType.Application.Json)
        }.body()
    }
}