package com.example.healthassistant.domain.model.assessment

data class CauseDetail(
    val aboutThis: List<String>,
    val percentage: Int,
    val commonDescription: String,
    val whatYouCanDoNow: List<String>,
    val warning: String?
)
