package com.example.healthassistant.data.local.assessment

import com.example.healthassistant.data.remote.assessment.dto.AnswerDto
import com.example.healthassistant.domain.model.assessment.Question

interface AssessmentLocalDataSource {

    suspend fun insertContext(
        question: Question,
        answer: AnswerDto
    )

    suspend fun getAllContext(): List<LocalContext>

    suspend fun clear()
}
