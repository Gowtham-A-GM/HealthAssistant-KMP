package com.example.healthassistant.domain.repository

import com.example.healthassistant.data.remote.assessment.dto.AnswerDto
import com.example.healthassistant.domain.model.assessment.AssessmentSession
import com.example.healthassistant.domain.model.assessment.Question
import com.example.healthassistant.domain.model.assessment.Report
import com.example.healthassistant.presentation.assessment.model.AssessmentPhase
import com.example.healthassistant.presentation.assessment.model.AssessmentUiModel

interface AssessmentRepository {

    suspend fun startAssessment(): AssessmentSession

    suspend fun submitAnswer(
        question: Question,
        answer: AnswerDto
    ): AssessmentSession?


    suspend fun submitFinalReport(): Report

    suspend fun getProfileAnswer(questionId: String): AnswerDto?




}


