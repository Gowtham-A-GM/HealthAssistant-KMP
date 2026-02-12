package com.example.healthassistant.data.local.assessment

interface AssessmentLocalDataSource {

    suspend fun saveAnswer(
        questionId: String,
        questionText: String,
        options: List<String>,
        selectedAnswer: String
    )

    suspend fun getAllAnswers(): List<LocalAnswer>

    suspend fun clear()
}
