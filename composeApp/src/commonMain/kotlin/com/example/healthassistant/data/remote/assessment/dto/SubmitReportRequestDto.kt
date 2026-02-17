package com.example.healthassistant.data.remote.assessment.dto

import kotlinx.serialization.Serializable

@Serializable
data class SubmitReportRequestDto(
    val responses: List<SimpleResponseDto>
)

@Serializable
data class SimpleResponseDto(
    val question: String,
    val answer: String
)
