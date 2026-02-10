package com.example.healthassistant.presentation.assessment.data

import com.example.healthassistant.presentation.assessment.data.dto.AnswerRequestDto
import com.example.healthassistant.presentation.assessment.data.dto.AnswerValueDto
import com.example.healthassistant.presentation.assessment.data.dto.ContextRequestDto
import com.example.healthassistant.presentation.assessment.data.mapper.toUiModel
import com.example.healthassistant.presentation.assessment.model.AssessmentPhase
import com.example.healthassistant.presentation.assessment.model.AssessmentUiModel
import com.example.healthassistant.util.AppLogger

class AssessmentRepositoryImpl(
    private val api: AssessmentApi
) : AssessmentRepository {

    private var currentSessionId: String = ""   // ✅ ADD THIS

    override suspend fun startSession(): AssessmentUiModel {
        AppLogger.d("API", "GET /session/start")
        val responseDto = api.startSession()

        // IMPORTANT: store session id for future requests
        currentSessionId = responseDto.session_id

        AppLogger.d("API", "INIT response sessionId=$currentSessionId")
        return responseDto.toUiModel()
    }

    override suspend fun submitAnswer(
        phase: AssessmentPhase,
        questionId: String,
        answerValue: String
    ): AssessmentUiModel {


        val answerType =
            if (phase == AssessmentPhase.LLM) "text" else "option"

        AppLogger.d(
            "API",
            "POST /chat phase=${phase.name.lowercase()} qId=$questionId type=$answerType value=$answerValue"
        )


        val response = api.submitAnswer(
            AnswerRequestDto(
                session_id = currentSessionId,
                phase = phase.name.lowercase(),
                question_id = questionId, // 🔥 REQUIRED
                answer = AnswerValueDto(
                    type = answerType,
                    value = answerValue
                )
            )
        )


        AppLogger.d("API", "Response phase=${response.phase}")
        return response.toUiModel()
    }

    override suspend fun pushNewUserContext(): AssessmentUiModel {
        AppLogger.d("API", "Calling pushContext()")

        val response = api.pushContext(
            ContextRequestDto(
                session_id = currentSessionId,
                user_choice = "new_user",
                questionnaire_context = null,
                medical_report = null
            )
        )

        AppLogger.d("API", "Context response phase=${response.phase}")
        return response.toUiModel()
    }



}
