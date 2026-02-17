package com.example.healthassistant.data.remote.assessment.dto

import kotlinx.serialization.Serializable

@Serializable
data class SubmitAnswerResponseDto(
    val session_id: String,
    val status: String? = null,
    val question: QuestionDto? = null
)
