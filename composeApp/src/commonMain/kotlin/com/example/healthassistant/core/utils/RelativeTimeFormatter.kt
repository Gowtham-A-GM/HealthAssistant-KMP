package com.example.healthassistant.core.utils

object RelativeTimeFormatter {

    fun format(isoTime: String?): String {

        if (isoTime.isNullOrBlank()) return "Recently"

        val articleMillis = DateTime.parseIsoToMillis(isoTime)
        val nowMillis = DateTime.getCurrentTimeMillis()

        val diffSeconds = (nowMillis - articleMillis) / 1000

        return when {
            diffSeconds < 60 ->
                "Just now"

            diffSeconds < 3600 ->
                "${diffSeconds / 60} minutes ago"

            diffSeconds < 86400 ->
                "${diffSeconds / 3600} hours ago"

            diffSeconds < 604800 ->
                "${diffSeconds / 86400} days ago"

            else ->
                "Recently"
        }
    }
}
