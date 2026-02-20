package com.example.healthassistant.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state

    // 🔥 Start Chat Session
    fun startChat(currentReportId: String? = null) {
        AppLogger.d("CHAT_VM", "StartChat triggered → reportId=$currentReportId")


        viewModelScope.launch {

            _state.value = _state.value.copy(isLoading = true)

            try {
                val firstMessage =
                    repository.startChat(currentReportId)

                _state.value = _state.value.copy(
                    isLoading = false,
                    sessionId = firstMessage.sessionId,
                    messages = listOf(firstMessage)
                )

                AppLogger.d("CHAT_VM", "Chat started: ${firstMessage.sessionId}")

            } catch (e: Exception) {

                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to start chat"
                )
            }
        }
    }

    // 🔥 Handle UI Events
    fun onEvent(event: ChatEvent) {

        when (event) {

            is ChatEvent.MessageChanged -> {
                _state.value = _state.value.copy(
                    typedMessage = event.text
                )
            }

            ChatEvent.SendMessage -> {
                sendMessage()
            }

            ChatEvent.Retry -> {
                startChat()
            }
        }
    }

    // 🔥 Send Message
    private fun sendMessage() {
        AppLogger.d("CHAT_VM", "SendMessage triggered")

        val sessionId = _state.value.sessionId ?: return
        val message = _state.value.typedMessage.trim()

        if (message.isEmpty()) return

        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                typedMessage = ""
            )

            try {

                val assistantReply =
                    repository.sendMessage(sessionId, message)

                val updatedHistory =
                    repository.getMessages(sessionId)

                _state.value = _state.value.copy(
                    isLoading = false,
                    messages = updatedHistory
                )

            } catch (e: Exception) {

                AppLogger.d("CHAT_VM", "Send error → ${e.message}")

                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to send message"
                )
            }
        }
    }

    fun endChat() {
        AppLogger.d("CHAT_VM", "Ending chat session")

        val sessionId = _state.value.sessionId ?: return

        viewModelScope.launch {
            try {
                repository.endChat(sessionId)
            } catch (e: Exception) {
                AppLogger.d("CHAT_VM", "End chat error → ${e.message}")
            }

            _state.value = ChatState()
        }
    }
}
