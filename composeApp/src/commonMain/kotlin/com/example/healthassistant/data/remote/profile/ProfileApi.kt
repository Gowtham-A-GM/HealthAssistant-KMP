package com.example.healthassistant.data.remote.profile

import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerRequestDto
import com.example.healthassistant.data.remote.profile.dto.ProfileResponseDto

interface ProfileApi {

    suspend fun submitOnboardingProfile(
        token: String,
        request: ProfileAnswerRequestDto
    ): ProfileResponseDto

    suspend fun submitMedicalOnboarding(
        token: String,
        request: ProfileAnswerRequestDto
    ): ProfileResponseDto
}