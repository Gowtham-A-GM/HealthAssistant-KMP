package com.example.healthassistant.domain.repository

import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerRequestDto
import com.example.healthassistant.data.remote.profile.dto.ProfileResponseDto

interface ProfileRepository {
    suspend fun submitProfile(
        token: String,
        request: ProfileAnswerRequestDto
    ): ProfileResponseDto

    suspend fun submitMedical(
        token: String,
        request: ProfileAnswerRequestDto
    ): ProfileResponseDto
}