package com.example.healthassistant.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.healthassistant.core.tts.TextToSpeechManager
import java.util.Locale

class AndroidTextToSpeechManager(
    context: Context
) : TextToSpeechManager {

    private var tts: TextToSpeech? = null
    private var pendingLanguageCode: String? = null

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val lang = pendingLanguageCode ?: "en"
                applyLocale(lang)
                pendingLanguageCode = null
            }
        }
    }

    private fun applyLocale(languageCode: String) {
        val tag = when (languageCode) {
            "hi" -> "hi-IN"
            "mr" -> "mr-IN"
            "ta" -> "ta-IN"
            "te" -> "te-IN"
            "ml" -> "ml-IN"
            "kn" -> "kn-IN"
            "bn" -> "bn-IN"
            "ur" -> "ur-IN"
            "gu" -> "gu-IN"
            "or" -> "or-IN"
            "pa" -> "pa-IN"
            "as" -> "as-IN"
            else -> "en-US"
        }
        val locale = Locale.forLanguageTag(tag)
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.setLanguage(Locale.US)
        }
    }

    override fun setLanguage(languageCode: String) {
        if (tts == null) {
            pendingLanguageCode = languageCode
        } else {
            applyLocale(languageCode)
        }
    }

    override fun speak(text: String) {
        tts?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "TTS_ID"
        )
    }

    override fun stop() {
        tts?.stop()
    }

    override fun shutdown() {
        tts?.shutdown()
    }
}
