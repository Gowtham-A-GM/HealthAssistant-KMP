package com.example.healthassistant.stt

interface SpeechToTextManager {

    fun startListening(
        onResult: (String) -> Unit,
        onError: (Throwable) -> Unit = {}
    )

    fun stopListening()

    fun isListening(): Boolean
}
