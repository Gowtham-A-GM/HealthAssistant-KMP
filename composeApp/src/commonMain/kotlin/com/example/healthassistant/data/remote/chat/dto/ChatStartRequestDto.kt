package com.example.healthassistant.data.remote.chat.dto

import ChatReportWrapperDto
import kotlinx.serialization.Serializable

@Serializable
data class ChatStartRequestDto(
    val profile_data: List<ProfileDataDto>,
    val reports: List<ChatReportWrapperDto>
)