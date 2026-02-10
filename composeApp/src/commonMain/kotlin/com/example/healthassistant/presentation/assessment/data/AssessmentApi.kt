package com.example.healthassistant.presentation.assessment.data

import com.example.healthassistant.presentation.assessment.data.dto.AnswerRequestDto
import com.example.healthassistant.presentation.assessment.data.dto.AssessmentResponseDto
import com.example.healthassistant.presentation.assessment.data.dto.ContextRequestDto

interface AssessmentApi {

    suspend fun startSession(): AssessmentResponseDto

    suspend fun pushContext(
        request: ContextRequestDto
    ): AssessmentResponseDto

    suspend fun submitAnswer(
        request: AnswerRequestDto
    ): AssessmentResponseDto
}
