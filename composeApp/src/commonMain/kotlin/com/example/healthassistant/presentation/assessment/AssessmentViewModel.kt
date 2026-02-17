package com.example.healthassistant.presentation.assessment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.domain.repository.AssessmentRepository
import com.example.healthassistant.core.stt.SpeechToTextManager
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.core.tts.TextToSpeechManager
import com.example.healthassistant.data.remote.assessment.dto.AnswerDto
import com.example.healthassistant.domain.model.assessment.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssessmentViewModel(
    private val repository: AssessmentRepository,
    private val speechToTextManager: SpeechToTextManager,
    private val ttsManager: TextToSpeechManager
) : ViewModel() {

    private val _state = MutableStateFlow(AssessmentState(isLoading = true))
    val state: StateFlow<AssessmentState> = _state


    // ✅ START ASSESSMENT
    fun startAssessment() {


        AppLogger.d("VM", "startAssessment() called")

        viewModelScope.launch {

            AppLogger.d("VM", "Setting loading = true")

            _state.value = _state.value.copy(isLoading = true)

            try {
                AppLogger.d("VM", "Calling repository.startAssessment()")

                val session = repository.startAssessment()

                AppLogger.d(
                    "VM",
                    "START SUCCESS → sessionId=${session.sessionId}, question=${session.question.text}}"
                )

                _state.value = _state.value.copy(
                    sessionId = session.sessionId
                )

                handleIncomingQuestion(session.question)



            } catch (e: Exception) {

                AppLogger.d("VM", "START ERROR → ${e.message}")

                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Unable to start assessment"
                )
            }
        }
    }


    // ✅ EVENTS
    fun onEvent(event: AssessmentEvent) {
        when (event) {

            is AssessmentEvent.TextChanged -> {
                _state.value = _state.value.copy(
                    typedText = event.text
                )
            }


            is AssessmentEvent.OptionSelected -> {
                val question = _state.value.currentQuestion ?: return
                AppLogger.d("VM", "Option selected: ${event.optionId}")
                if (_state.value.isCompleted) return

                submitSingleChoiceAnswer(question, event.optionId)
            }

            is AssessmentEvent.SendText -> {
                val question = _state.value.currentQuestion ?: return
                val text = _state.value.typedText
                if (text.isNotBlank()) {
                    if (_state.value.isCompleted) return

                    submitTextAnswer(question, text)
                }
            }


            is AssessmentEvent.MicClicked -> {
                startListening()
            }

            is AssessmentEvent.StopListening -> {
                stopListening()
            }

            is AssessmentEvent.SpeechRecognized -> {
                _state.value = _state.value.copy(
                    recognizedSpeech = event.text,
                    typedText = event.text
                )
            }

            is AssessmentEvent.VolumeClicked -> {

                val newMuteState = !_state.value.isMuted

                if (newMuteState) {
                    ttsManager.stop()
                }

                _state.value = _state.value.copy(
                    isMuted = newMuteState
                )
            }


            AssessmentEvent.ExitClicked -> {
                AppLogger.d("VM", "Exit clicked")
            }

            else -> {
                // Ignore old events like MyselfSelected, etc.
            }
        }
    }

    private fun submitTextAnswer(question: Question, text: String) {

        ttsManager.stop()

        viewModelScope.launch {

            _state.value = _state.value.copy(isLoading = true)

            try {
                val answerDto = AnswerDto(
                    type = question.responseType,
                    value = text
                )

                val session = repository.submitAnswer(
                    question = question,
                    answer = answerDto
                )

                // 🔥 IF backend says completed
                if (session == null) {

                    _state.value = _state.value.copy(
                        isGeneratingReport = true,
                        currentQuestion = null
                    )

                    generateReport()
                    return@launch
                }


                // Otherwise show next question
                handleIncomingQuestion(session.question)


            } catch (e: Exception) {

                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to submit answer"
                )
            }
        }
    }


    private fun submitSingleChoiceAnswer(
        question: Question,
        selectedOptionId: String
    ) {
        ttsManager.stop()

        viewModelScope.launch {

            _state.value = _state.value.copy(isLoading = true)

            try {
                val selectedOption = question.responseOptions
                    ?.find { it.id == selectedOptionId }

                val answerDto = AnswerDto(
                    type = "single_choice",
                    selected_option_id = selectedOptionId,
                    selected_option_label = selectedOption?.label
                )

                val session = repository.submitAnswer(
                    question = question,
                    answer = answerDto
                )

                // 🔥 IF backend says completed
                if (session == null) {

                    _state.value = _state.value.copy(
                        isGeneratingReport = true,
                        currentQuestion = null
                    )

                    generateReport()
                    return@launch
                }


                handleIncomingQuestion(session.question)


            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to submit answer"
                )
            }
        }
    }


    private fun speakQuestionIfNeeded(question: Question) {

        if (_state.value.isMuted) return

        val textBuilder = StringBuilder()

        textBuilder.append(question.text)

        question.responseOptions?.let { options ->
            options.forEachIndexed { index, option ->
                textBuilder.append(". Option ${index + 1}. ${option.label}")
            }
        }

        ttsManager.speak(textBuilder.toString())
    }


    private fun generateReport() {
        ttsManager.stop()

        viewModelScope.launch {

            _state.value = _state.value.copy(isLoading = true)

            try {
                val report = repository.submitFinalReport()

                AppLogger.d("VM", "REPORT SUCCESS → id=${report.reportId}")

                _state.value = _state.value.copy(
                    isLoading = false,
                    isCompleted = true,
                    report = report,
                    currentQuestion = null
                )

            } catch (e: Exception) {

                AppLogger.d("VM", "REPORT ERROR → ${e.message}")

                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to generate report"
                )
            }
        }
    }






    // ✅ ERROR CLEAR
    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.stop()
        ttsManager.shutdown()
    }


    // ✅ SPEECH
    private fun startListening() {
        _state.value = _state.value.copy(isListening = true)

        speechToTextManager.startListening(
            onResult = { text ->
                onEvent(AssessmentEvent.SpeechRecognized(text))
            },
            onError = {
                _state.value = _state.value.copy(isListening = false)
            }
        )
    }

    private fun stopListening() {
        speechToTextManager.stopListening()
        _state.value = _state.value.copy(isListening = false)
    }

    private suspend fun handleIncomingQuestion(question: Question) {

        // If optional → check stored profile answer
        if (!question.isCompulsory) {

            val stored = repository.getProfileAnswer(question.id)

            if (stored != null) {

                AppLogger.d("VM", "AUTO ANSWERING → ${question.id}")

                val session = repository.submitAnswer(
                    question = question,
                    answer = stored
                )

                if (session == null) {
                    generateReport()
                    return
                }

                // Recursively check next question
                handleIncomingQuestion(session.question)
                return
            }
        }

        // Otherwise show UI normally
        _state.value = _state.value.copy(
            isLoading = false,
            currentQuestion = question,
            typedText = "",
            errorMessage = null
        )

        speakQuestionIfNeeded(question)
    }

}
