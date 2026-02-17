package com.example.healthassistant.domain.model.assessment

data class Report(
    val reportId: String,
    val topic: String,
    val generatedAt: String,
    val patientInfo: PatientInfo,
    val summary: List<String>,
    val possibleCauses: List<PossibleCause>,
    val advice: List<String>,
    val urgencyLevel: String
)
