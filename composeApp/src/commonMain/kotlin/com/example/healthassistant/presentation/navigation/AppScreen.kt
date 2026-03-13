package com.example.healthassistant.presentation.navigation

import com.example.healthassistant.domain.model.assessment.PossibleCause


sealed class AppScreen {

    object Welcome : AppScreen()
    object Login : AppScreen()
    object Signup : AppScreen()

    object OnboardingProfile : AppScreen()
    object OnboardingMedical : AppScreen()

    object Home : AppScreen()

    object Settings : AppScreen()

    object EditProfile : AppScreen()
    object EditMedical : AppScreen()
    object Language : AppScreen()

    object Assessment : AppScreen()

    object AssessmentReport : AppScreen()

    data class AssessmentCauseDetail(
        val cause: PossibleCause
    ) : AppScreen()

    object History : AppScreen()
    object News : AppScreen()

    data class HistoryDetail(val reportId: String) : AppScreen()
    data class CauseDetail(val cause: PossibleCause) : AppScreen()

    data class Chat(val reportId: String?) : AppScreen()
}
