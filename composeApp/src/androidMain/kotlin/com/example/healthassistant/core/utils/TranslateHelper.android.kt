package com.example.healthassistant.core.utils

import android.content.Context
import com.example.healthassistant.core.translator.AutoTranslator

actual suspend fun platformTranslate(text: String): String {
    return AutoTranslator.translate(text)
}

actual fun platformInitTranslator(lang: String) {
    AutoTranslator.init(lang)
}

actual fun saveLanguagePref(lang: String) {
    appContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .edit().putString("language", lang).apply()
}

actual fun loadLanguagePref(): String {
    return appContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .getString("language", "en") ?: "en"
}