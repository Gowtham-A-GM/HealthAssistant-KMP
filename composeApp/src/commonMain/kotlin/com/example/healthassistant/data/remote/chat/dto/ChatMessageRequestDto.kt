package com.example.healthassistant.data.remote.chat.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequestDto(
    val session_id: String,
    val history: List<ChatHistoryDto>
)

@Serializable
data class ChatHistoryDto(
    val role: String,   // "user" or "assistant"
    val content: String
)
