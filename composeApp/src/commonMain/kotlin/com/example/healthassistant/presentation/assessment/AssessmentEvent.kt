package com.example.healthassistant.presentation.assessment

enum class VitalsField { HEART_RATE, SPO2, TEMPERATURE }

sealed class AssessmentEvent {

    object MyselfSelected : AssessmentEvent()
    object SomeoneElseSelected : AssessmentEvent()
    data class OptionSelected(
        val optionId: String
    ) : AssessmentEvent()

    data class TextChanged(val text: String) : AssessmentEvent()

    object SendText : AssessmentEvent()
    object SendImage : AssessmentEvent()
    object MicClicked : AssessmentEvent()
    object StopListening : AssessmentEvent()

    data class SpeechRecognized(val text: String) : AssessmentEvent()
    object VolumeClicked : AssessmentEvent()


    object SkipQuestion : AssessmentEvent()
    object RealtimeDataClicked : AssessmentEvent()
    data class VitalsFieldChanged(val field: VitalsField, val value: String) : AssessmentEvent()
    object ConfirmRealtimeVitals : AssessmentEvent()

    object ExitClicked : AssessmentEvent()

    object OpenVisualMode : AssessmentEvent()
    object CloseVisualMode : AssessmentEvent()
    data class BodyPartSelected(val partId: String) : AssessmentEvent()
    object DismissBottomSheet : AssessmentEvent()
    object VisualBackPressed : AssessmentEvent()
    data class VisualSymptomSelected(
        val bodyPath: List<String>,
        val symptomId: String,
        val symptomLabel: String
    ) : AssessmentEvent()

}
