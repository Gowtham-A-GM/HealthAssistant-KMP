package com.example.healthassistant.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.core.utils.extractReadableValue
import com.example.healthassistant.data.local.profile.GeneralProfileLocalDataSource
import com.example.healthassistant.data.local.report.ReportLocalDataSource
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class HomeViewModel(
    private val profileLocal: GeneralProfileLocalDataSource,
    private val reportLocal: ReportLocalDataSource,
    private val onStartAssessment: () -> Unit,
    private val onOpenChat: () -> Unit,
    private val onOpenSettings: () -> Unit,
    private val onOpenLastReport: ((reportId: String) -> Unit)? = null
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private val _navigateToReportId = MutableStateFlow<String?>(null)
    val navigateToReportId: StateFlow<String?> = _navigateToReportId

    init {
        loadProfileData()
        loadSymptomChips()
    }

    private fun loadSymptomChips() {
        viewModelScope.launch {
            try {
                val reports = reportLocal.getAll()
                val counts = reports
                    .groupingBy { it.topic.trim() }
                    .eachCount()
                val chips = counts.entries
                    .sortedByDescending { it.value }
                    .map { (topic, count) -> "$count x $topic" }
                _state.value = _state.value.copy(suggestions = chips)
            } catch (_: Exception) { /* ignore */ }
        }
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
        loadSymptomChips()
    }

    fun clearNavigateToReport() {
        _navigateToReportId.value = null
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
                if (event.item.title == "Previous Check") {
                    viewModelScope.launch {
                        try {
                            val lastReport = reportLocal.getAll()
                                .maxByOrNull { it.generatedAt }
                            if (lastReport != null) {
                                onOpenLastReport?.invoke(lastReport.reportId)
                                _navigateToReportId.value = lastReport.reportId
                            }
                        } catch (_: Exception) { /* ignore */ }
                    }
                }
            }
        }
    }
}
