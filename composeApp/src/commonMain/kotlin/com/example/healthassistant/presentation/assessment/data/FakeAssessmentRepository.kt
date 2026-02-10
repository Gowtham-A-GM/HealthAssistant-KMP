//package com.example.healthassistant.presentation.assessment.data
//
//import com.example.healthassistant.presentation.assessment.model.AnswerUiModel
//import com.example.healthassistant.presentation.assessment.model.QuestionUiModel
//
//class FakeAssessmentRepository : AssessmentRepository {
//
//    private var step = 1
//
//    override suspend fun loadFirstQuestion(): QuestionUiModel {
//        return QuestionUiModel(
//            id = "q1",
//            title = "Abdominal pain",
//            step = 1,
//            totalSteps = 3,
//            question = "How long have you had this pain?",
//            options = listOf(
//                AnswerUiModel("1", "Less than 1 day"),
//                AnswerUiModel("2", "2–3 days"),
//                AnswerUiModel("3", "More than a week")
//            )
//        )
//    }
//
//    override suspend fun submitAnswer(
//        questionId: String,
//        optionId: String
//    ): QuestionUiModel {
//        step++
//
//        return QuestionUiModel(
//            id = "q$step",
//            title = "Abdominal pain",
//            step = step,
//            totalSteps = 3,
//            question = "Is the pain mild or severe?",
//            options = listOf(
//                AnswerUiModel("1", "Mild"),
//                AnswerUiModel("2", "Moderate"),
//                AnswerUiModel("3", "Severe")
//            )
//        )
//    }
//}
