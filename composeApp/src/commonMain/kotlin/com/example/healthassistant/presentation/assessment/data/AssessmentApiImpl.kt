package com.example.healthassistant.presentation.assessment.data

import com.example.healthassistant.presentation.assessment.data.dto.AnswerRequestDto
import com.example.healthassistant.presentation.assessment.data.dto.AssessmentResponseDto
import com.example.healthassistant.presentation.assessment.data.dto.ContextRequestDto
import com.example.healthassistant.util.AppLogger
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

    override suspend fun startSession(): AssessmentResponseDto {
        return client
            .get("$baseUrl/session/start")
            .body()
    }

    override suspend fun submitAnswer(
        request: AnswerRequestDto
    ): AssessmentResponseDto {
        return client
            .post("$baseUrl/chat") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            .body()
    }

    override suspend fun pushContext(
        request: ContextRequestDto
    ): AssessmentResponseDto {

        AppLogger.d("API", "POST /session/context body=$request")

        return client.post("$baseUrl/session/context") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

}
