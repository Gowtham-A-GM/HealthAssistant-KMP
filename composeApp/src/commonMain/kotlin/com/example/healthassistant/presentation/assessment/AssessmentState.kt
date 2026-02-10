package com.example.healthassistant.presentation.assessment

import com.example.healthassistant.presentation.assessment.model.AnswerUiModel
import com.example.healthassistant.presentation.assessment.model.AssessmentPhase

data class AssessmentState(
    val phase: AssessmentPhase = AssessmentPhase.INIT,
    val isLoading: Boolean = false,

    // INIT
    val requestContext: Boolean = false,
    val requestQuestionnaire: Boolean = false,

    // PREDEFINED
    val questionId: String = "",          // ✅ ADD THIS
    val title: String = "",
    val step: Int = 0,
    val totalSteps: Int = 0,
    val question: String = "",
    val typedText: String = "",
    val options: List<AnswerUiModel> = emptyList(),

    // LLM
    val assistantMessage: String? = null,
    val analysisHeadline: String? = null,
    val analysisAdvice: List<String> = emptyList(),
    val actionOptions: List<AnswerUiModel> = emptyList(),


    // Voice / UX
    val isListening: Boolean = false,     // ✅ ADD THIS
    val recognizedSpeech: String = "",

    // Common
    val errorMessage: String? = null
)
