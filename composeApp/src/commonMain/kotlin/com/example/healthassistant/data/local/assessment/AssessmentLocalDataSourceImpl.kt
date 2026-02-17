package com.example.healthassistant.data.local.assessment

import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.data.remote.assessment.dto.AnswerDto
import com.example.healthassistant.db.HealthDatabase
import com.example.healthassistant.domain.model.assessment.Question
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class AssessmentLocalDataSourceImpl(
    database: HealthDatabase
) : AssessmentLocalDataSource {

    private val queries = database.assessmentContextQueries

    override suspend fun insertContext(
        question: Question,
        answer: AnswerDto
    ) {
        AppLogger.d(
            "DB",
            "INSERT → qId=${question.id}, type=${question.responseType}"
        )

        val optionsJson = question.responseOptions?.let {
            Json.encodeToString(it)
        }

        val answerJson = Json.encodeToString(answer)

        queries.insertContext(
            question_id = question.id,
            question_text = question.text,
            response_type = question.responseType,
            response_options_json = optionsJson,
            answer_json = answerJson
        )
    }

    override suspend fun getAllContext(): List<LocalContext> {
        return queries.selectAll().executeAsList().map {
            LocalContext(
                questionId = it.question_id,
                questionText = it.question_text,
                responseType = it.response_type,
                responseOptionsJson = it.response_options_json,
                answerJson = it.answer_json
            )
        }
    }

    override suspend fun clear() {
        queries.clearAll()
    }
}

