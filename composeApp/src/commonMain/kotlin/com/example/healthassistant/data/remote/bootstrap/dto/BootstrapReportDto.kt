package com.example.healthassistant.data.remote.bootstrap.dto

import kotlinx.serialization.Serializable

@Serializable
data class BootstrapReportDto(
    val id: String,
    val report_json: String,
    val date: String
)