package com.example.healthassistant.data.remote.assessment

import com.example.healthassistant.data.remote.assessment.dto.AnswerRequestDto
import com.example.healthassistant.data.remote.assessment.dto.AssessmentResponseDto
import com.example.healthassistant.data.remote.assessment.dto.ContextRequestDto
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.data.remote.assessment.dto.ReportResponseDto
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

        val response: StartAssessmentResponseDto =
            client.get("$baseUrl/assessment/start").body()

        AppLogger.d(
            "API",
            "Response received → session_id=${response.session_id}}"
        )

        return response
    }

    override suspend fun submitAnswer(
        request: SubmitAnswerRequestDto
    ): SubmitAnswerResponseDto {

        val response: SubmitAnswerResponseDto =
            client.post("$baseUrl/assessment/answer") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()

        AppLogger.d("API", "Response status → ${response.status}")

        return response
    }


    override suspend fun submitReport(
        request: SubmitReportRequestDto
    ): ReportResponseDto {
        return client.post("$baseUrl/assessment/report") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }






}
