package com.example.healthassistant.presentation.assessment.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ContextRequestDto(
    val session_id: String,
    val user_choice: String, // "new_user" | "existing_user"
    val questionnaire_context: QuestionnaireContextDto? = null,
    val medical_report: MedicalReportDto? = null
)

@Serializable
data class QuestionnaireContextDto(
    val demographics: DemographicsDto,
    val medical_history: List<String> = emptyList(),
    val allergies: List<String> = emptyList()
)

@Serializable
data class DemographicsDto(
    val age: Int,
    val gender: String
)

@Serializable
data class MedicalReportDto(
    val report_id: String,
    val summary: String,
    val last_urgency: String
)
