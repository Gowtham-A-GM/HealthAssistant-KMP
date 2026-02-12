package com.example.healthassistant.data.remote.assessment

import com.example.healthassistant.data.remote.assessment.dto.AnswerRequestDto
import com.example.healthassistant.data.remote.assessment.dto.AssessmentResponseDto
import com.example.healthassistant.data.remote.assessment.dto.ContextRequestDto

interface AssessmentApi {

    suspend fun startSession(): AssessmentResponseDto

    suspend fun pushContext(
        request: ContextRequestDto
    ): AssessmentResponseDto

    suspend fun submitAnswer(
        request: AnswerRequestDto
    ): AssessmentResponseDto
}
