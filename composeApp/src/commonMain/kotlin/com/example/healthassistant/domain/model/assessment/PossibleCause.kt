package com.example.healthassistant.domain.model.assessment

data class PossibleCause(
    val id: String,
    val title: String,
    val shortDescription: String,
    val subtitle: String?,
    val severity: String,
    val probability: Double,
    val detail: CauseDetail
)
