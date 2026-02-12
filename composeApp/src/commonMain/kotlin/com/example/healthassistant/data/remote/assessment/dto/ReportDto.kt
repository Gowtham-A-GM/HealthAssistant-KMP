package com.example.healthassistant.data.remote.assessment.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportDto(
    val report_id: String,
    val summary: List<String>,
    val analysis: AnalysisDto
)
