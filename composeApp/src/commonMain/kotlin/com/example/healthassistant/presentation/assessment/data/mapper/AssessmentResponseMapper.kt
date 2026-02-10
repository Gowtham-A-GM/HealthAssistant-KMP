package com.example.healthassistant.presentation.assessment.data.mapper

import com.example.healthassistant.presentation.assessment.data.dto.AssessmentResponseDto
import com.example.healthassistant.presentation.assessment.model.AssessmentPhase
import com.example.healthassistant.presentation.assessment.model.AssessmentUiModel
import com.example.healthassistant.presentation.assessment.model.AnswerUiModel

fun AssessmentResponseDto.toUiModel(): AssessmentUiModel {
    return when (phase) {

        "init" -> AssessmentUiModel(
            phase = AssessmentPhase.INIT,
            requestContext = request_context == true,
            requestQuestionnaire = request_questionnaire == true
        )

        "predefined" -> AssessmentUiModel(
            phase = AssessmentPhase.PREDEFINED,
            questionId = question?.question_id.orEmpty(), // 👈 important
            question = question?.text.orEmpty(),
            step = progress?.current ?: 0,
            totalSteps = progress?.total ?: 0,
            options = options?.map {
                AnswerUiModel(it.id, it.label)
            } ?: emptyList()
        )


        "llm" -> {
            if (analysis != null) {
                AssessmentUiModel(
                    phase = AssessmentPhase.LLM,
                    analysisHeadline = analysis.headline,
                    analysisAdvice = analysis.advice,
                    actionOptions = action_prompt?.options?.map {
                        AnswerUiModel(it.id, it.label)
                    } ?: emptyList()
                )
            } else {
                AssessmentUiModel(
                    phase = AssessmentPhase.LLM,
                    assistantMessage = message
                )
            }
        }


        "report" -> AssessmentUiModel(
            phase = AssessmentPhase.REPORT,
            reportSummary = report?.analysis?.headline
        )

        else -> AssessmentUiModel(
            phase = AssessmentPhase.END
        )
    }
}
