package com.example.healthassistant.presentation.assessment.data.dto

@kotlinx.serialization.Serializable
data class ProgressDto(
    val current: Int,
    val total: Int
)