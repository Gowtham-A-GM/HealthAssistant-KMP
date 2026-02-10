package com.example.healthassistant.presentation.home

data class HomeState(
    val userName: String = "Gowtham",
    val suggestions: List<String> = listOf(
        "I have fever",
        "My head hurts",
        "Coughing",
        "Stomach pain",
        "I'm feeling very tired from morning"
    )
)
