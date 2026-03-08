package com.example.healthassistant.presentation.settings

data class EditProfileState(

    val answers: Map<String, String> = emptyMap(),
    val profileImageBase64: String? = null,

    val isLoading: Boolean = false,

    val isSuccess: Boolean = false,

    val errorMessage: String? = null
)