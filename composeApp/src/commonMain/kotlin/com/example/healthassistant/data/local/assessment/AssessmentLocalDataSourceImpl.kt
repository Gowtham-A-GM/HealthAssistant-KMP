package com.example.healthassistant.data.local.assessment

import com.example.healthassistant.db.HealthDatabase
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
class AssessmentLocalDataSourceImpl(
    database: HealthDatabase
) : AssessmentLocalDataSource {

    private val queries = database.assessmentQueries

    override suspend fun saveAnswer(
        questionId: String,
        questionText: String,
        options: List<String>,
        selectedAnswer: String
    ) {
        queries.insertAnswer(
            question_id = questionId,
            question_text = questionText,
            options_json = Json.encodeToString(options),
            selected_answer = selectedAnswer
        )
    }

    override suspend fun getAllAnswers(): List<LocalAnswer> {
        val rows = queries.selectAll().executeAsList()

        return rows.map { row ->
            LocalAnswer(
                questionId = row.question_id,
                questionText = row.question_text,
                options = Json.decodeFromString<List<String>>(row.options_json),
                selectedAnswer = row.selected_answer
            )
        }
    }

    override suspend fun clear() {
        queries.clearAll()
    }
}

