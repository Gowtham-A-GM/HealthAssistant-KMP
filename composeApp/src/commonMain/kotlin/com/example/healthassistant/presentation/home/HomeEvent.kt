package com.example.healthassistant.presentation.home

sealed class HomeEvent {
    object StartAssessment : HomeEvent()
    data class SuggestionClicked(val text: String) : HomeEvent()
    object EmergencyClicked : HomeEvent()
}
