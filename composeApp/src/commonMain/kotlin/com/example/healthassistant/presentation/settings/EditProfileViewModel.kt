package com.example.healthassistant.presentation.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.core.utils.extractReadableValue
import com.example.healthassistant.data.local.profile.GeneralProfileLocalDataSource
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerDto
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerItemDto
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerRequestDto
import com.example.healthassistant.domain.repository.ProfileRepository
import com.example.healthassistant.presentation.auth.questions.ProfileQuestionConfig
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EditProfileViewModel(
    private val local: GeneralProfileLocalDataSource,
    private val repository: ProfileRepository
) : ViewModel() {

    var state = mutableStateOf(EditProfileState())
        private set

    init {
        loadLocalProfile()
    }

    private fun loadLocalProfile() {

        viewModelScope.launch {

            AppLogger.section("EDIT_PROFILE_VM", "Loading Local Profile")

            val rows = local.getAll()

            AppLogger.d("EDIT_PROFILE_VM", "Rows count → ${rows.size}")

            val answersMap = mutableMapOf<String, String>()
            var imageBase64: String? = null

            rows.forEach { row ->

                AppLogger.d(
                    "EDIT_PROFILE_VM",
                    "Row → ${row.question_id} : ${row.answer_json}"
                )
                AppLogger.d(
                    "EDIT_PROFILE_VM",
                    "Parsed → ${row.question_id} : ${extractReadableValue(row.answer_json)}"
                )

                if (row.question_id == "q_profile_image") {

                    imageBase64 = extractReadableValue(row.answer_json)

                } else {

                    val parsed = extractReadableValue(row.answer_json)

                    answersMap[row.question_id] =
                        if (parsed == "Not Applicable") "" else parsed
                }
            }

            state.value = state.value.copy(
                answers = answersMap,
                profileImageBase64 = imageBase64
            )
        }
    }

    fun updateAnswer(id: String, value: String) {

        state.value = state.value.copy(
            answers = state.value.answers + (id to value)
        )
    }

    fun updateProfileImage(base64: String) {

        state.value = state.value.copy(
            profileImageBase64 = base64
        )
    }

    fun saveProfile() {

        viewModelScope.launch {

            state.value = state.value.copy(isLoading = true)

            try {

                val request = buildRequest()

                val response = repository.submitProfile(request)

                if (response.success) {

                    // update local db
                    local.clearAll()

                    request.answer_json.forEach {

                        local.insert(
                            questionId = it.question_id,
                            questionText = it.question_text,
                            answerJson = Json.encodeToString(it.answer_json)
                        )
                    }

                    state.value = state.value.copy(isSuccess = true)

                } else {

                    state.value = state.value.copy(
                        errorMessage = response.message
                    )
                }

            } catch (e: Exception) {

                AppLogger.d("EDIT_PROFILE", e.message ?: "error")

                state.value = state.value.copy(
                    errorMessage = e.message
                )
            }

            state.value = state.value.copy(isLoading = false)
        }
    }

    private fun buildRequest(): ProfileAnswerRequestDto {

        val allQuestions =
            ProfileQuestionConfig.questions +
                    ProfileQuestionConfig.femaleConditional

        val answerList = mutableListOf<ProfileAnswerItemDto>()

        // 1️⃣ PROFILE IMAGE

        state.value.profileImageBase64?.let { base64 ->

            answerList.add(
                ProfileAnswerItemDto(
                    question_id = "q_profile_image",
                    question_text = "Profile Image",
                    answer_json = ProfileAnswerDto(
                        type = "image",
                        value = base64
                    )
                )
            )
        }

        // 2️⃣ NORMAL QUESTIONS

        state.value.answers.forEach { (id, value) ->

            val question = allQuestions.firstOrNull { it.id == id }
                ?: return@forEach

            val dto = when (question.type) {

                "text" -> ProfileAnswerDto(
                    type = "text",
                    value = value
                )

                "single_choice" -> ProfileAnswerDto(
                    type = "single_choice",
                    selected_option_label = value
                )

                else -> ProfileAnswerDto(
                    type = question.type,
                    value = value
                )
            }

            answerList.add(
                ProfileAnswerItemDto(
                    question_id = id,
                    question_text = question.questionText,
                    answer_json = dto
                )
            )
        }

        // 3️⃣ AGE (NUMBER)

        state.value.answers["q_age"]?.toIntOrNull()?.let { age ->

            answerList.removeAll { it.question_id == "q_age" }

            answerList.add(
                ProfileAnswerItemDto(
                    question_id = "q_age",
                    question_text = "Age",
                    answer_json = ProfileAnswerDto(
                        type = "number",
                        number_value = age
                    )
                )
            )
        }

        return ProfileAnswerRequestDto(
            answer_json = answerList
        )
    }
    fun reload() {
        loadLocalProfile()
    }
}