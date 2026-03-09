package com.example.healthassistant.data.local.profile

import com.example.healthassistant.data.remote.assessment.dto.AnswerDto
import com.example.healthassistant.db.GeneralProfile

interface GeneralProfileLocalDataSource {

    suspend fun insert(
        questionId: String,
        questionText: String,
        answerJson: String
    )

    suspend fun getAll(): List<GeneralProfile>

    suspend fun getByQuestionId(questionId: String): GeneralProfile?

    suspend fun deleteByQuestionId(questionId: String)

    suspend fun clearAll()
}