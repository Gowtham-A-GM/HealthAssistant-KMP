package com.example.healthassistant.data.local.profile

import com.example.healthassistant.db.MedicalProfile

interface MedicalProfileLocalDataSource {

    suspend fun insert(
        questionId: String,
        questionText: String,
        answerJson: String
    )

    suspend fun getAll(): List<MedicalProfile>

    suspend fun clearAll()
}