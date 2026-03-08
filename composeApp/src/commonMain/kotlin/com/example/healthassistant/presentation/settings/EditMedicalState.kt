package com.example.healthassistant.presentation.settings

data class EditMedicalState(

    val answers: Map<String, String> = emptyMap(),

    val isSuccess: Boolean = false
)