package com.example.healthassistant.data.repository

import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.data.local.assessment.AssessmentLocalDataSource
import com.example.healthassistant.data.local.profile.ProfileLocalDataSource
//import com.example.healthassistant.data.local.assessment.AssessmentLocalDataSource
import com.example.healthassistant.data.remote.assessment.AssessmentApi
import com.example.healthassistant.data.remote.assessment.dto.AnswerDto
import com.example.healthassistant.data.remote.assessment.dto.AnswerRequestDto
import com.example.healthassistant.data.remote.assessment.dto.AnswerValueDto
import com.example.healthassistant.data.remote.assessment.dto.ContextRequestDto
import com.example.healthassistant.data.remote.assessment.dto.QuestionDto
import com.example.healthassistant.data.remote.assessment.dto.ResponseOptionDto
import com.example.healthassistant.data.remote.assessment.dto.SimpleResponseDto
import com.example.healthassistant.data.remote.assessment.dto.SubmitAnswerRequestDto
import com.example.healthassistant.data.remote.assessment.dto.SubmitReportRequestDto
import com.example.healthassistant.data.remote.assessment.mapper.toDomain
import com.example.healthassistant.data.remote.assessment.mapper.toUiModel
import com.example.healthassistant.domain.model.assessment.AssessmentSession
import com.example.healthassistant.domain.model.assessment.Question
import com.example.healthassistant.domain.model.assessment.Report
import com.example.healthassistant.domain.model.assessment.ResponseOption
import com.example.healthassistant.domain.repository.AssessmentRepository
import com.example.healthassistant.presentation.assessment.model.AssessmentPhase
import com.example.healthassistant.presentation.assessment.model.AssessmentUiModel
import kotlinx.serialization.json.Json

class AssessmentRepositoryImpl(
    private val api: AssessmentApi,
    private val sessionLocal: AssessmentLocalDataSource,
    private val profileLocal: ProfileLocalDataSource
)
 : AssessmentRepository {


    private var currentSessionId: String = ""


    override suspend fun startAssessment(): AssessmentSession {

        AppLogger.d("REPO", "startAssessment() called")

        val dto = api.startAssessment()

        AppLogger.d(
            "REPO",
            "API Response → session_id=${dto.session_id}, question_id=${dto.question.question_id}"
        )

        currentSessionId = dto.session_id

        val domain = dto.toDomain()

        AppLogger.d(
            "REPO",
            "Mapped to Domain → question=${domain.question.text}"
        )

        return domain
    }

    override suspend fun submitAnswer(
        question: Question,
        answer: AnswerDto
    ): AssessmentSession? {

        val request = SubmitAnswerRequestDto(
            session_id = currentSessionId,
            question = QuestionDto(
                question_id = question.id,
                text = question.text,
                response_type = question.responseType,
                response_options = question.responseOptions?.map {
                    ResponseOptionDto(it.id, it.label)
                },
                is_compulsory = question.isCompulsory
            ),
            answer = answer
        )

        val response = api.submitAnswer(request)

        // Always store locally
        // Always store for current session
        sessionLocal.insertContext(question, answer)

        // Store permanently ONLY if optional
        if (!question.isCompulsory) {
            profileLocal.insertOrUpdate(question, answer)
        }


        return if (response.status == "completed") {
            null
        } else {
            // next question case
            AssessmentSession(
                sessionId = currentSessionId,
                question = response.question!!.toDomain())
        }

    }


    override suspend fun submitFinalReport(): Report {

        val stored = sessionLocal.getAllContext()



        val responses = stored.map { localItem ->

            val answerDto = Json.decodeFromString<AnswerDto>(
                localItem.answerJson
            )

            val readableAnswer = when (answerDto.type) {

                "text", "number" -> {
                    answerDto.value ?: ""
                }

                "single_choice" -> {
                    answerDto.selected_option_label ?: ""
                }

                "multi_choice" -> {
                    answerDto.selected_option_labels
                        ?.joinToString(", ") ?: ""
                }

                else -> ""
            }

            SimpleResponseDto(
                question = localItem.questionText,
                answer = readableAnswer
            )
        }

        val request = SubmitReportRequestDto(
            responses = responses
        )

        AppLogger.d("REPO", "FINAL REPORT REQUEST → $request")

        val response = api.submitReport(request)

        AppLogger.d("REPO", "REPORT API SUCCESS → clearing local DB")

// ✅ CLEAR DB HERE (not in ViewModel)
        sessionLocal.clear()


        return response.toDomain()

    }

    override suspend fun getProfileAnswer(questionId: String): AnswerDto? {
        return profileLocal.getAnswer(questionId)
    }







}