package com.example.healthassistant.presentation.navigation

import com.example.healthassistant.domain.model.assessment.PossibleCause


sealed class AppScreen {
    object Home : AppScreen()
    object Assessment : AppScreen()

    object AssessmentReport : AppScreen()

    data class AssessmentCauseDetail(
        val cause: PossibleCause
    ) : AppScreen()




    object History : AppScreen()
    object News : AppScreen()

//    object AssessmentStart : AppScreen()

    object HistoryDetail : AppScreen()
    data class CauseDetail(val title: String) : AppScreen()
}
