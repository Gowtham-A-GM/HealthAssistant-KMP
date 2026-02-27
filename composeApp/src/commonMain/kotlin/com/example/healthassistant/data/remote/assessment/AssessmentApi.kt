package com.example.healthassistant.data.remote.assessment

import com.example.healthassistant.data.remote.assessment.dto.ReportDto
import com.example.healthassistant.data.remote.assessment.dto.StartAssessmentResponseDto
import com.example.healthassistant.data.remote.assessment.dto.SubmitAnswerRequestDto
import com.example.healthassistant.data.remote.assessment.dto.SubmitAnswerResponseDto
import com.example.healthassistant.data.remote.assessment.dto.SubmitReportRequestDto

interface AssessmentApi {

    suspend fun startAssessment(): StartAssessmentResponseDto

    suspend fun submitAnswer(
        request: SubmitAnswerRequestDto
    ): SubmitAnswerResponseDto

    suspend fun submitReport(
        request: SubmitReportRequestDto
    ): ReportDto

    suspend fun endSession(sessionId: String)

    suspend fun getUserReports(): List<ReportDto>
}