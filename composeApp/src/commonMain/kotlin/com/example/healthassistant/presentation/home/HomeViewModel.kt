package com.example.healthassistant.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.core.utils.extractReadableValue
import com.example.healthassistant.data.local.profile.GeneralProfileLocalDataSource
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class HomeViewModel(
    private val profileLocal: GeneralProfileLocalDataSource,
    private val onStartAssessment: () -> Unit,
    private val onOpenChat: () -> Unit,
    private val onOpenSettings: () -> Unit
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            try {
                val name = profileLocal.getByQuestionId("q_name")
                    ?.answer_json
                    ?.let { extractReadableValue(it) }
                    ?.takeIf { it.isNotBlank() } ?: ""

                val image = try {
                    profileLocal.getByQuestionId("q_profile_image")
                        ?.answer_json
                        ?.let { extractReadableValue(it) }
                        ?.takeIf { it.isNotBlank() }
                } catch (e: Exception) {
                    // Oversized image already in DB — remove it so future getAll() won't crash
                    profileLocal.deleteByQuestionId("q_profile_image")
                    null
                }

                val contactNumbers = try {
                    val row = profileLocal.getByQuestionId("q_emergency_contacts")
                    if (row != null) {
                        val dto = Json.decodeFromString<ProfileAnswerDto>(row.answer_json)
                        val regex = Regex(""""number":"([^"]+)"""")
                        regex.findAll(dto.value ?: "").map { it.groupValues[1] }.toList()
                    } else emptyList()
                } catch (_: Exception) { emptyList() }

                _state.value = _state.value.copy(
                    userName = name,
                    profileImageBase64 = image,
                    emergencyContactNumbers = contactNumbers
                )
            } catch (e: Exception) {
                // Fallback: try without image
                try {
                    val name = profileLocal.getByQuestionId("q_name")
                        ?.answer_json
                        ?.let { extractReadableValue(it) }
                        ?.takeIf { it.isNotBlank() } ?: ""
                    _state.value = _state.value.copy(userName = name)
                } catch (_: Exception) { /* ignore */ }
            }
        }
    }

    fun refreshProfileData() {
        loadProfileData()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {

            HomeEvent.OpenChat -> {
                onOpenChat()
            }
            HomeEvent.SettingsClicked -> {
                onOpenSettings()
            }

            HomeEvent.StartAssessment -> {
                onStartAssessment()
            }

            is HomeEvent.SuggestionClicked -> {
                // handle later
            }

            is HomeEvent.QuickHelpClicked -> {
                // handle later
            }
        }
    }
}
