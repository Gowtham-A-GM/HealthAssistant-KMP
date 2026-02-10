package com.example.healthassistant.presentation.assessment.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportDto(
    val report_id: String,
    val summary: List<String>,
    val analysis: AnalysisDto
)
