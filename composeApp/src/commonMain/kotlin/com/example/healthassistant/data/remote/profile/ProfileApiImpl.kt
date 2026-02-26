package com.example.healthassistant.data.remote.profile

import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.data.remote.auth.dto.*
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerRequestDto
import com.example.healthassistant.data.remote.profile.dto.ProfileResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class ProfileApiImpl(
    private val client: HttpClient,
    private val baseUrl: String
) : ProfileApi {

    override suspend fun submitOnboardingProfile(
        token: String,
        request: ProfileAnswerRequestDto
    ): ProfileResponseDto {

        val response = client.post("$baseUrl/user/profile/onboarding") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
            setBody(request)
        }

        return response.body()
    }

    override suspend fun submitMedicalOnboarding(
        token: String,
        request: ProfileAnswerRequestDto
    ): ProfileResponseDto {

        val response = client.post("$baseUrl/user/medical/onboarding") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
            setBody(request)
        }

        return response.body()
    }
}