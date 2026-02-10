package com.example.healthassistant.presentation.assessment.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnswerOptionDto(
    val id: String,
    val label: String
)
