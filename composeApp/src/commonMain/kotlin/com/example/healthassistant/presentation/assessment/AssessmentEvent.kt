package com.example.healthassistant.presentation.assessment

sealed class AssessmentEvent {

    object MyselfSelected : AssessmentEvent()
    object SomeoneElseSelected : AssessmentEvent()
    data class OptionSelected(
        val optionId: String
    ) : AssessmentEvent()

    data class TextChanged(val text: String) : AssessmentEvent()

    object SendText : AssessmentEvent()
    object MicClicked : AssessmentEvent()
    object StopListening : AssessmentEvent()

    data class SpeechRecognized(val text: String) : AssessmentEvent()

    object ExitClicked : AssessmentEvent()
}
