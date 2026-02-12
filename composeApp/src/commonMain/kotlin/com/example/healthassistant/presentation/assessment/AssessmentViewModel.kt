package com.example.healthassistant.presentation.assessment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.domain.repository.AssessmentRepository
import com.example.healthassistant.presentation.assessment.model.AssessmentPhase
import com.example.healthassistant.presentation.assessment.model.AssessmentUiModel
import com.example.healthassistant.core.stt.SpeechToTextManager
import com.example.healthassistant.core.logger.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class AssessmentViewModel(
    private val repository: AssessmentRepository,
    private val speechToTextManager: SpeechToTextManager
) : ViewModel() {

    private val _state = MutableStateFlow(AssessmentState(isLoading = true))
    private val sessionAnswers = mutableMapOf<String, String>()
    val state: StateFlow<AssessmentState> = _state

    init {
        startSession()
    }

    private fun startSession() {
        AppLogger.d("VM", "startSession()")
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                AppLogger.d("VM", "Calling repository.startSession()")
                val uiModel = repository.startSession()
                AppLogger.d("VM", "Received INIT → ${uiModel.phase}")
                applyUiModel(uiModel)

            } catch (e: Exception) {
                AppLogger.d("VM", "ERROR startSession: ${e.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Unable to start session. Please try again."
                )
            }
        }
    }


    fun onEvent(event: AssessmentEvent) {
        AppLogger.d("VM", "Event: $event")
        when (event) {

            is AssessmentEvent.MyselfSelected -> {
                handleMyselfFlow()
            }

            is AssessmentEvent.SomeoneElseSelected -> {
                handleSomeoneElseFlow()
            }

            is AssessmentEvent.TextChanged -> {
                AppLogger.d("VM", "TextChanged: ${event.text}")
                _state.value = _state.value.copy(
                    typedText = event.text
                )
            }

            is AssessmentEvent.OptionSelected -> {
                AppLogger.d("VM", "OptionSelected: ${event.optionId}")
                submitAnswer(event.optionId)
            }

            is AssessmentEvent.SendText -> {
                AppLogger.d("VM", "SendText: ${state.value.typedText}")
                if (state.value.typedText.isNotBlank()) {
                    submitAnswer(state.value.typedText)
                }
            }


            is AssessmentEvent.MicClicked -> {
                AppLogger.d("VM", "MicClicked")
                startListening()
            }

            is AssessmentEvent.StopListening -> {
                stopListening()
            }

            is AssessmentEvent.SpeechRecognized -> {
                AppLogger.d("VM", "SpeechRecognized: ${event.text}")
                _state.value = _state.value.copy(
                    recognizedSpeech = event.text,
                    typedText = event.text

                )
            }

            AssessmentEvent.ExitClicked -> {
                AppLogger.d("VM", "ExitClicked")
            }
        }
    }

    private fun handleMyselfFlow() {
        viewModelScope.launch {

            repository.setIsMyselfSession(true)

            val ui = repository.pushContext(null)

            applyUiModel(ui)
        }
    }


    private fun handleSomeoneElseFlow() {
        viewModelScope.launch {

            repository.setIsMyselfSession(false)

//            repository.clearStoredAnswers()  // 🔥 CLEAR OLD DATA

            val ui = repository.pushContext(null)

            applyUiModel(ui)
        }
    }



    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }


    private fun submitAnswer(answerValue: String) {
        AppLogger.d("VM", "submitAnswer() phase=${_state.value.phase} value=$answerValue")
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                if (_state.value.phase == AssessmentPhase.PREDEFINED) {
                    sessionAnswers[_state.value.questionId] = answerValue
                }

                val uiModel = repository.submitAnswer(
                    phase = _state.value.phase,
                    questionId = _state.value.questionId, // 🔥 REQUIRED
                    answerValue = answerValue
                )

                AppLogger.d("VM", "submitAnswer response → ${uiModel.phase}")
                applyUiModel(uiModel)

            } catch (e: Exception) {
                AppLogger.d("VM", "ERROR submitAnswer: ${e.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Network error. Please try again."
                )
            }
        }
    }

    private fun applyUiModel(ui: AssessmentUiModel) {

        AppLogger.d("LLM_CHECK", "assistantMessage=${ui.assistantMessage}")


        AppLogger.d("DEBUG",
            "requestContext=${ui.requestContext}, requestQuestionnaire=${ui.requestQuestionnaire}, question='${ui.question}'"
        )


        AppLogger.d(
            "STATE",
            "phase=${ui.phase}, qId=${ui.questionId}, question=${ui.question}"
        )

        if (
            ui.phase == AssessmentPhase.PREDEFINED &&
            ui.requestContext &&
            ui.requestQuestionnaire &&
            ui.question.isBlank()
        ) {
            AppLogger.d("VM", "Predefined finished → pushing questionnaire context")
            pushQuestionnaireContext()
            return
        }

        _state.value = _state.value.copy(
            phase = ui.phase,
            requestContext = ui.requestContext,
            requestQuestionnaire = ui.requestQuestionnaire,
            questionId = ui.questionId,
            question = ui.question,
            typedText = "",
            options = ui.options,
            step = ui.step,
            totalSteps = ui.totalSteps,
            assistantMessage = ui.assistantMessage,
            analysisHeadline = ui.analysisHeadline,
            analysisAdvice = ui.analysisAdvice,
            actionOptions = ui.actionOptions,
            recognizedSpeech = "",
            isLoading = false
        )

        // ✅ CASE 1: INIT → show CHOOSE_USER screen
        if (ui.phase == AssessmentPhase.INIT && ui.requestContext) {
            AppLogger.d("VM", "INIT → show choose user screen")

            _state.value = _state.value.copy(
                phase = AssessmentPhase.CHOOSE_USER
            )
            return
        }

        // ✅ CASE 2: PREDEFINED finished → now push questionnaire
        if (
            ui.phase == AssessmentPhase.PREDEFINED &&
            ui.requestContext &&
            ui.requestQuestionnaire &&
            ui.question.isBlank()
        ) {
            AppLogger.d("VM", "Predefined finished → pushing questionnaire context")
            pushQuestionnaireContext()
            return
        }
    }


    private fun pushQuestionnaireContext() {
        viewModelScope.launch {

            val uiModel = repository.pushContext(sessionAnswers)

            applyUiModel(uiModel)
        }
    }




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


}
