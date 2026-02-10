package com.example.healthassistant.presentation.assessment.model

data class AssessmentUiModel(
    val phase: AssessmentPhase,

    // INIT
    val requestContext: Boolean = false,
    val requestQuestionnaire: Boolean = false,

    // PREDEFINED
    val questionId: String = "",
    val title: String = "",
    val step: Int = 0,
    val totalSteps: Int = 0,
    val question: String = "",
    val options: List<AnswerUiModel> = emptyList(),

    // LLM
    val assistantMessage: String? = null,
    val analysisHeadline: String? = null,
    val analysisAdvice: List<String> = emptyList(),
    val actionOptions: List<AnswerUiModel> = emptyList(),

    // REPORT
    val reportSummary: String? = null
)
