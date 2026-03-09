package com.example.healthassistant.presentation.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.core.utils.extractReadableValue
import com.example.healthassistant.data.local.profile.MedicalProfileLocalDataSource
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerDto
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerItemDto
import com.example.healthassistant.data.remote.profile.dto.ProfileAnswerRequestDto
import com.example.healthassistant.domain.repository.ProfileRepository
import com.example.healthassistant.presentation.auth.questions.MedicalQuestionConfig
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EditMedicalViewModel(
    private val local: MedicalProfileLocalDataSource,
    private val repository: ProfileRepository
) : ViewModel() {

    var state = mutableStateOf(EditMedicalState())
        private set

    init {
        loadLocalMedical()
    }

    private fun loadLocalMedical() {

        viewModelScope.launch {

            AppLogger.section("EDIT_MEDICAL_VM", "Loading Medical from Local DB")

            val rows = local.getAll()

            AppLogger.d("EDIT_MEDICAL_VM", "Rows count → ${rows.size}")

            val map = mutableMapOf<String, String>()

            rows.forEach { row ->

                AppLogger.d(
                    "EDIT_MEDICAL_VM",
                    "DB Row → ${row.question_id} : ${row.answer_json}"
                )

                val parsedValue = extractReadableValue(row.answer_json)

                AppLogger.d(
                    "EDIT_MEDICAL_VM",
                    "Parsed Value → ${row.question_id} : $parsedValue"
                )

                map[row.question_id] = parsedValue
            }

            AppLogger.d("EDIT_MEDICAL_VM", "Final Map → $map")

            state.value = state.value.copy(
                answers = map
            )
        }
    }

    fun updateAnswer(id: String, value: String) {

        state.value = state.value.copy(
            answers = state.value.answers + (id to value)
        )
    }

    fun saveMedical() {

        viewModelScope.launch {

            val request = buildRequest()
            AppLogger.logJson("EDIT_MEDICAL_VM", "REQUEST", request)

            val response = repository.submitMedical(request)

            if (response.success) {

                // Update local DB via UPSERT (INSERT OR REPLACE) — no clearAll
                // so DB is never left empty if a write fails mid-way.
                request.answer_json.forEach {

                    local.insert(
                        questionId = it.question_id,
                        questionText = it.question_text,
                        answerJson = Json.encodeToString(it.answer_json)
                    )
                }

                state.value = state.value.copy(isSuccess = true)
            }
        }
    }

    private fun buildRequest(): ProfileAnswerRequestDto {

        val answerList = state.value.answers.map { (id, value) ->

            val questionConfig =
                MedicalQuestionConfig.questions.first { it.id == id }

            ProfileAnswerItemDto(
                question_id = id,
                question_text = questionConfig.questionText,
                answer_json = ProfileAnswerDto(
                    type = questionConfig.type,
                    value = if (questionConfig.type == "text") value else null,
                    selected_option_label =
                        if (questionConfig.type == "single_choice") value else null
                )
            )
        }

        return ProfileAnswerRequestDto(answer_json = answerList)
    }
    fun reload() {
        loadLocalMedical()
    }

    fun resetForReopen() {
        state.value = state.value.copy(isSuccess = false)
    }
}