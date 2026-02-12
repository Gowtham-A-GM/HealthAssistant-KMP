package com.example.healthassistant.domain.repository

import com.example.healthassistant.presentation.assessment.model.AssessmentPhase
import com.example.healthassistant.presentation.assessment.model.AssessmentUiModel

interface AssessmentRepository {

    suspend fun startSession(): AssessmentUiModel

    suspend fun submitAnswer(
        phase: AssessmentPhase,
        questionId: String,
        answerValue: String
    ): AssessmentUiModel

    suspend fun pushContext(
        questionnaireContext: Map<String, String>?
    ): AssessmentUiModel


    fun setIsMyselfSession(value: Boolean)

    suspend fun clearStoredAnswers()

}

