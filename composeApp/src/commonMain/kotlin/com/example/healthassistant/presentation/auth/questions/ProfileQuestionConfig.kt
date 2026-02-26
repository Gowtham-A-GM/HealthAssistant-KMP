package com.example.healthassistant.presentation.auth.questions

import com.example.healthassistant.presentation.auth.model.QuestionUiModel

object ProfileQuestionConfig {

    val questions = listOf(

        // 🔹 BASIC IDENTITY

        QuestionUiModel(
            id = "q_name",
            questionText = "What is your full name?",
            type = "text"
        ),

        QuestionUiModel(
            id = "q_age",
            questionText = "What is your age group?",
            type = "single_choice",
            options = listOf(
                "under_18",
                "18_25",
                "26_35",
                "36_45",
                "46_55",
                "56_65",
                "66_75",
                "over_75"
            )
        ),

        QuestionUiModel(
            id = "q_gender",
            questionText = "What is your sex assigned at birth?",
            type = "single_choice",
            options = listOf(
                "male",
                "female",
                "intersex",
                "prefer_not_to_say"
            )
        ),

        // 🔹 CONTACT INFO

        QuestionUiModel(
            id = "q_city",
            questionText = "Which city do you live in?",
            type = "text"
        ),

        QuestionUiModel(
            id = "q_emergency_relation",
            questionText = "Who is your emergency contact?",
            type = "single_choice",
            options = listOf(
                "father",
                "mother",
                "spouse",
                "sibling",
                "friend",
                "guardian",
                "other"
            )
        ),

        QuestionUiModel(
            id = "q_emergency_number",
            questionText = "Emergency contact phone number",
            type = "text"
        ),

        // 🔹 PHYSICAL INFO

        QuestionUiModel(
            id = "q_blood_group",
            questionText = "What is your blood group?",
            type = "single_choice",
            options = listOf(
                "a_positive",
                "a_negative",
                "b_positive",
                "b_negative",
                "ab_positive",
                "ab_negative",
                "o_positive",
                "o_negative",
                "unknown"
            )
        ),

        QuestionUiModel(
            id = "q_height_range",
            questionText = "Select your height range",
            type = "single_choice",
            options = listOf(
                "below_150_cm",
                "150_160_cm",
                "161_170_cm",
                "171_180_cm",
                "above_180_cm"
            )
        ),

        QuestionUiModel(
            id = "q_weight_range",
            questionText = "Select your weight range",
            type = "single_choice",
            options = listOf(
                "below_50_kg",
                "50_60_kg",
                "61_70_kg",
                "71_80_kg",
                "above_80_kg"
            )
        ),

        // 🔹 LIFESTYLE

        QuestionUiModel(
            id = "q_occupation",
            questionText = "What is your occupation?",
            type = "single_choice",
            options = listOf(
                "student",
                "working_professional",
                "self_employed",
                "homemaker",
                "retired",
                "unemployed",
                "other"
            )
        )
    )

    // 🔹 CONDITIONAL (Female)

    val femaleConditional = listOf(

        QuestionUiModel(
            id = "q_pregnancy_status",
            questionText = "What is your pregnancy status?",
            type = "single_choice",
            options = listOf(
                "pregnant",
                "trying_to_conceive",
                "postpartum",
                "breastfeeding",
                "menopausal",
                "not_applicable"
            )
        ),

        QuestionUiModel(
            id = "q_menstrual_status",
            questionText = "What is your menstrual cycle status?",
            type = "single_choice",
            options = listOf(
                "regular_cycle",
                "irregular_cycle",
                "currently_menstruating",
                "missed_period",
                "not_applicable"
            )
        )
    )
}