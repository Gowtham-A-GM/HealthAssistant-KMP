package com.example.healthassistant.presentation.auth.questions

import com.example.healthassistant.presentation.auth.model.QuestionUiModel

object MedicalQuestionConfig {

    val questions = listOf(

        QuestionUiModel(
            id = "q_triggers",
            questionText = "What makes your symptoms worse?",
            type = "single_choice",
            options = listOf(
                "physical_activity",
                "eating",
                "lying_down",
                "standing_up",
                "stress",
                "certain_foods",
                "nothing_specific",
                "not_applicable"
            )
        ),

        QuestionUiModel(
            id = "q_relieving_factors",
            questionText = "What makes your symptoms better?",
            type = "single_choice",
            options = listOf(
                "rest",
                "medication",
                "eating",
                "changing_position",
                "nothing_helps",
                "not_sure"
            )
        ),

        QuestionUiModel(
            id = "q_med_history",
            questionText = "Do you have any past medical conditions?",
            type = "single_choice",
            options = listOf(
                "diabetes",
                "hypertension",
                "heart_disease",
                "asthma_copd",
                "kidney_disease",
                "liver_disease",
                "cancer_history",
                "autoimmune_disease",
                "mental_health_conditions",
                "multiple_conditions",
                "none"
            )
        ),

        QuestionUiModel(
            id = "q_med_history_details",
            questionText = "Please list all your medical conditions:",
            type = "text"
        ),

        QuestionUiModel(
            id = "q_surgeries",
            questionText = "Have you had any surgeries?",
            type = "single_choice",
            options = listOf("yes", "no")
        ),

        QuestionUiModel(
            id = "q_family_history",
            questionText = "Any significant family medical history?",
            type = "single_choice",
            options = listOf(
                "heart_disease",
                "diabetes",
                "cancer",
                "stroke",
                "mental_illness",
                "autoimmune_disease",
                "multiple_conditions",
                "none_known"
            )
        ),

        QuestionUiModel(
            id = "q_current_medications",
            questionText = "Are you currently taking any medications?",
            type = "single_choice",
            options = listOf(
                "prescription_medications",
                "over_the_counter_only",
                "herbal_supplements",
                "multiple_medications",
                "none"
            )
        ),

        QuestionUiModel(
            id = "q_medication_list",
            questionText = "Please list all medications and supplements:",
            type = "text"
        ),

        QuestionUiModel(
            id = "q_allergies",
            questionText = "Do you have any known allergies?",
            type = "single_choice",
            options = listOf(
                "medication_allergy",
                "food_allergy",
                "environmental_allergy",
                "latex_allergy",
                "multiple_allergies",
                "none_known"
            )
        ),

        QuestionUiModel(
            id = "q_allergy_details",
            questionText = "Please specify your allergies and reactions:",
            type = "text"
        ),

        QuestionUiModel(
            id = "q_smoking_status",
            questionText = "What is your smoking status?",
            type = "single_choice",
            options = listOf(
                "never_smoked",
                "former_smoker",
                "current_smoker",
                "vaping_only"
            )
        ),

        QuestionUiModel(
            id = "q_alcohol_use",
            questionText = "How often do you consume alcohol?",
            type = "single_choice",
            options = listOf(
                "never",
                "rarely",
                "socially",
                "regularly",
                "daily"
            )
        ),

        QuestionUiModel(
            id = "q_recent_events",
            questionText = "Have you experienced any recent events?",
            type = "single_choice",
            options = listOf(
                "recent_injury",
                "recent_travel",
                "recent_illness",
                "new_medication",
                "dietary_change",
                "stress_event",
                "exposure_to_sick_person",
                "insect_bite",
                "none"
            )
        ),

        QuestionUiModel(
            id = "q_previous_treatment",
            questionText = "Have you already tried any treatment?",
            type = "single_choice",
            options = listOf(
                "home_remedies",
                "over_the_counter_meds",
                "prescription_meds",
                "nothing_yet",
                "multiple_treatments"
            )
        ),

        QuestionUiModel(
            id = "q_treatment_details",
            questionText = "What treatments have you tried?",
            type = "text"
        )
    )
}