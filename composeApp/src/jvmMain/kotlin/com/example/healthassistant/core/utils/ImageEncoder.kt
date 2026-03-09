package com.example.healthassistant.core.utils

actual fun compressAndEncodeProfileImage(bytes: ByteArray): String {
    // Desktop/JVM: no platform compression — fall back to plain base64
    return encodeImageToBase64(bytes)
}

actual suspend fun platformTranslate(text: String): String = text
actual fun platformInitTranslator(lang: String) {}
actual fun saveLanguagePref(lang: String) {}
actual fun loadLanguagePref(): String = "en"
