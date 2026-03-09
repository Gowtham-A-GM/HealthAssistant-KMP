package com.example.healthassistant.presentation.settings

import com.example.healthassistant.presentation.auth.model.EmergencyContact

data class EditProfileState(

    val answers: Map<String, String> = emptyMap(),
    val profileImageBase64: String? = null,
    val emergencyContacts: List<EmergencyContact> = listOf(EmergencyContact()),

    val isLoading: Boolean = false,

    val isSuccess: Boolean = false,

    val errorMessage: String? = null
)