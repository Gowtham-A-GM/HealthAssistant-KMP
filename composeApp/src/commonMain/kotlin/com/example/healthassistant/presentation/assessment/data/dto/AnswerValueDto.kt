package com.example.healthassistant.presentation.assessment.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnswerValueDto(
    val type: String,   // "option" | "text"
    val value: String
)
