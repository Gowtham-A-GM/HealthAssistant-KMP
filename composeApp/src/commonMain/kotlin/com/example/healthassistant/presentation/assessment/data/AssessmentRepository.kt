package com.example.healthassistant.presentation.assessment.data

import com.example.healthassistant.presentation.assessment.model.AssessmentPhase
import com.example.healthassistant.presentation.assessment.model.AssessmentUiModel

interface AssessmentRepository {
    suspend fun startSession(): AssessmentUiModel
    suspend fun pushNewUserContext(): AssessmentUiModel
    suspend fun submitAnswer(
        phase: AssessmentPhase,
        questionId: String,
        answerValue: String
    ): AssessmentUiModel

}


