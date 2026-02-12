package com.example.healthassistant.data.remote.assessment.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class QuestionDto(
    @SerialName("assessment_id")
    val assessmentId: String,

    @SerialName("question_id")
    val questionId: String,

    val title: String,
    val step: Int,
    val total_steps: Int,
    val question: String,
    val options: List<AnswerOptionDto>
)
