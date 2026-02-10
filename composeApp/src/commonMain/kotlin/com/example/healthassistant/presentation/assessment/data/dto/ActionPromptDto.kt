package com.example.healthassistant.presentation.assessment.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActionPromptDto(
    val question: String,
    val options: List<AnswerOptionDto>
)
