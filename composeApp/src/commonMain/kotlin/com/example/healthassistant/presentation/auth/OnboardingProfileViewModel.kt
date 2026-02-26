package com.example.healthassistant.presentation.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerDto
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerItemDto
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerRequestDto
import com.example.healthassistant.domain.repository.ProfileRepository
import com.example.healthassistant.presentation.auth.questions.ProfileQuestionConfig
import kotlinx.coroutines.launch

class OnboardingProfileViewModel(
    private val repository: ProfileRepository,
    private val token: String
) : ViewModel() {

    var state = mutableStateOf(OnboardingProfileState())
        private set

    fun onDynamicValueChange(id: String, value: String) {
        state.value = state.value.copy(
            answers = state.value.answers + (id to value)
        )
    }

    fun getValueForQuestion(id: String): String {
        return state.value.answers[id] ?: ""
    }

    fun submitProfile() {

        AppLogger.d("PROFILE_VM", "Token used: $token")

        // 🔹 VALIDATION (only required ones)
        val requiredQuestions = listOf("q_name", "q_age", "q_gender")

        for (questionId in requiredQuestions) {
            if (state.value.answers[questionId].isNullOrBlank()) {
                state.value = state.value.copy(
                    errorMessage = "Please complete all required fields"
                )
                return
            }
        }

        viewModelScope.launch {

            state.value = state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {

                val request = buildRequest()

                val response = repository.submitProfile(token, request)

                if (response.success) {
                    state.value = state.value.copy(isSuccess = true)
                } else {
                    state.value = state.value.copy(
                        errorMessage = response.message
                    )
                }

            } catch (e: Exception) {
                state.value = state.value.copy(
                    errorMessage = e.message ?: "Something went wrong"
                )
            }

            state.value = state.value.copy(isLoading = false)
        }
    }

    private fun buildRequest(): ProfileAnswerRequestDto {

        val allQuestions =
            ProfileQuestionConfig.questions +
                    ProfileQuestionConfig.femaleConditional

        val answerList = state.value.answers.map { (id, value) ->

            val question = allQuestions.first { it.id == id }

            ProfileAnswerItemDto(
                question_id = id,
                question_text = question.questionText,
                answer_json = ProfileAnswerDto(
                    type = question.type,
                    value = if (question.type == "text") value else null,
                    selected_option_label =
                        if (question.type == "single_choice") value else null
                )
            )
        }

        return ProfileAnswerRequestDto(answer_json = answerList)
    }

    fun resetSuccess() {
        state.value = state.value.copy(isSuccess = false)
    }
}