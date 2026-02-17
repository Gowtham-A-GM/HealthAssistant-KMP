package com.example.healthassistant.data.remote.assessment.dto

import kotlinx.serialization.Serializable

@Serializable
data class StartAssessmentResponseDto(
    val session_id: String,
    val question: QuestionDto
)
