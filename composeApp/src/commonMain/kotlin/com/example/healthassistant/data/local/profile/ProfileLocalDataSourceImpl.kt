package com.example.healthassistant.data.local.profile

import com.example.healthassistant.data.remote.assessment.dto.AnswerDto
import com.example.healthassistant.db.HealthDatabase
import com.example.healthassistant.domain.model.assessment.Question
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProfileLocalDataSourceImpl(
    private val database: HealthDatabase
) : ProfileLocalDataSource {

    override suspend fun insertOrUpdate(
        question: Question,
        answer: AnswerDto
    ) {
        val json = Json.encodeToString(answer)

        database.profileAnswersQueries.insertOrReplace(
            question_id = question.id,
            answer_json = json
        )
    }

    override suspend fun getAnswer(questionId: String): AnswerDto? {

        val row = database.profileAnswersQueries
            .getById(questionId)
            .executeAsOneOrNull()

        return row?.let {
            Json.decodeFromString<AnswerDto>(it.answer_json)
        }
    }
}
