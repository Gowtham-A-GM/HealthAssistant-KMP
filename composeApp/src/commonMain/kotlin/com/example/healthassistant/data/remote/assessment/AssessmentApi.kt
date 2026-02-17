package com.example.healthassistant.data.remote.assessment

import com.example.healthassistant.data.remote.assessment.dto.AnswerRequestDto
import com.example.healthassistant.data.remote.assessment.dto.AssessmentResponseDto
import com.example.healthassistant.data.remote.assessment.dto.ContextRequestDto
import com.example.healthassistant.data.remote.assessment.dto.ReportResponseDto
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
    ): ReportResponseDto



}
