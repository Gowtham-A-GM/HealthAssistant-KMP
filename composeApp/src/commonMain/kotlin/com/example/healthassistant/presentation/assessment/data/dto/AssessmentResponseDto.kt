package com.example.healthassistant.presentation.assessment.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssessmentResponseDto(
    val session_id: String,
    val phase: String,

    // INIT
    val request_context: Boolean? = null,
    val request_questionnaire: Boolean? = null,
    val supported_phases: List<String>? = null,

    // PREDEFINED
    val question: QuestionBlockDto? = null,
    val options: List<AnswerOptionDto>? = null,
    val progress: ProgressDto? = null,

    // LLM
    val message: String? = null,
    val analysis: AnalysisDto? = null,
    val action_prompt: ActionPromptDto? = null,

    // REPORT
    val report: ReportDto? = null
)
