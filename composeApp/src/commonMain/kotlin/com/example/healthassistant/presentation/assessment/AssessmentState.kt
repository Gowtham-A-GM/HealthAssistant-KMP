package com.example.healthassistant.presentation.assessment

import com.example.healthassistant.domain.model.assessment.Question
import com.example.healthassistant.domain.model.assessment.Report
import com.example.healthassistant.presentation.assessment.model.AnswerUiModel
import com.example.healthassistant.presentation.assessment.model.AssessmentPhase

data class AssessmentState(
    val isLoading: Boolean = false,
    val sessionId: String = "",
    val currentQuestion: Question? = null,
    val typedText: String = "",
    val recognizedSpeech: String = "",
    val isListening: Boolean = false,
    val isMuted: Boolean = false,
    val errorMessage: String? = null,
    val isCompleted: Boolean = false,
    val report: Report? = null,
    val isGeneratingReport: Boolean = false


)

