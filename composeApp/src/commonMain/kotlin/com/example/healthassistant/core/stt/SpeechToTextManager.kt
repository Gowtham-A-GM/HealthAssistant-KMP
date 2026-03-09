package com.example.healthassistant.core.stt

interface SpeechToTextManager {

    fun startListening(
        onResult: (String) -> Unit,
        onError: (Throwable) -> Unit = {}
    )

    fun stopListening()

    fun isListening(): Boolean

    fun setLanguage(languageCode: String)
}
