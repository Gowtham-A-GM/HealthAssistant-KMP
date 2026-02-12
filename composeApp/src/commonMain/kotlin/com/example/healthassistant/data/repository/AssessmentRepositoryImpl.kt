package com.example.healthassistant.data.repository

import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.data.local.assessment.AssessmentLocalDataSource
import com.example.healthassistant.data.remote.assessment.AssessmentApi
import com.example.healthassistant.data.remote.assessment.dto.AnswerRequestDto
import com.example.healthassistant.data.remote.assessment.dto.AnswerValueDto
import com.example.healthassistant.data.remote.assessment.dto.ContextRequestDto
import com.example.healthassistant.data.remote.assessment.mapper.toUiModel
import com.example.healthassistant.domain.repository.AssessmentRepository
import com.example.healthassistant.presentation.assessment.model.AssessmentPhase
import com.example.healthassistant.presentation.assessment.model.AssessmentUiModel

class AssessmentRepositoryImpl(
    private val api: AssessmentApi,
    private val local: AssessmentLocalDataSource
) : AssessmentRepository {

    private var currentSessionId: String = ""   // ✅ ADD THIS
    private var isMyselfSession: Boolean = false


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

        if (isMyselfSession && phase == AssessmentPhase.PREDEFINED) {
            local.saveAnswer(
                questionId = questionId,
                questionText = "",
                options = emptyList(),
                selectedAnswer = answerValue
            )
        }


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

    override suspend fun pushContext(
        questionnaireContext: Map<String, String>?
    ): AssessmentUiModel {

        val response = api.pushContext(
            ContextRequestDto(
                session_id = currentSessionId,
                user_choice = "new_user",
                questionnaire_context = questionnaireContext,
                medical_report = null
            )
        )

        return response.toUiModel()
    }

    override fun setIsMyselfSession(value: Boolean) {
        isMyselfSession = value
    }

    override suspend fun clearStoredAnswers() {
        local.clear()
    }

}