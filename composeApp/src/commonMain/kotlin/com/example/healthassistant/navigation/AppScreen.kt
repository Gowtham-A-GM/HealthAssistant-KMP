package com.example.healthassistant.navigation


sealed class AppScreen {
    object Home : AppScreen()
    object Assessment : AppScreen()
    object History : AppScreen()
    object News : AppScreen()

//    object AssessmentStart : AppScreen()

    object HistoryDetail : AppScreen()
    data class CauseDetail(val title: String) : AppScreen()
}
