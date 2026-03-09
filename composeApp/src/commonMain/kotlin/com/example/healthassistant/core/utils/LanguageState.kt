package com.example.healthassistant.core.utils

import androidx.compose.runtime.mutableStateOf

data class AppLanguage(
    val name: String,        // English display name
    val nativeName: String,  // Name in that language
    val code: String         // ML Kit / BCP-47 code
)

object LanguageState {

    val languages: List<AppLanguage> = listOf(
        AppLanguage("English",    "English",       "en"),
        AppLanguage("Hindi",      "हिंदी",          "hi"),
        AppLanguage("Marathi",    "मराठी",          "mr"),
        AppLanguage("Tamil",      "தமிழ்",          "ta"),
        AppLanguage("Telugu",     "తెలుగు",         "te"),
        AppLanguage("Malayalam",  "മലയാളം",        "ml"),
        AppLanguage("Kannada",    "ಕನ್ನಡ",          "kn"),
        AppLanguage("Bengali",    "বাংলা",          "bn"),
        AppLanguage("Urdu",       "اردو",           "ur"),
        AppLanguage("Gujarati",   "ગુજરાતી",        "gu"),
        AppLanguage("Odia",       "ଓଡ଼ିଆ",          "or"),
        AppLanguage("Punjabi",    "ਪੰਜਾਬੀ",         "pa"),
        AppLanguage("Assamese",   "অসমীয়া",        "as"),
    )

    val currentLanguage = mutableStateOf("en")

    fun currentLanguageName(): String =
        languages.firstOrNull { it.code == currentLanguage.value }?.name ?: "English"
}