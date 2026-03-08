package com.example.healthassistant.core.bodymap

/**
 * Represents a symptom option shown in the bottom sheet.
 */
data class SymptomOption(
    val id: String,
    val label: String
)

/**
 * Represents a sub-part of a body region (e.g., "Eye" under "Head").
 * Each sub-part has its own list of symptoms.
 */
data class SubPart(
    val id: String,
    val label: String,
    val symptoms: List<SymptomOption>
)

/**
 * Contains the symptom/sub-part data for a body region.
 * - Simple regions: only [symptoms] is populated.
 * - Complex regions (with sub-parts): both [symptoms] (general) and [subParts] are populated.
 */
data class BodyRegionData(
    val regionId: String,
    val label: String,
    val symptoms: List<SymptomOption> = emptyList(),
    val subParts: List<SubPart> = emptyList()
)

/**
 * Provides symptom/sub-part data for all body regions.
 * In the future this could come from the backend.
 */
object BodyRegionDataProvider {

    private val regionDataMap: Map<String, BodyRegionData> = mapOf(

        // ── HEAD (complex: has sub-parts) ──
        "body_region_head" to BodyRegionData(
            regionId = "body_region_head",
            label = "Head",
            symptoms = listOf(
                SymptomOption("head_pain", "Head pain"),
                SymptomOption("headache", "Headache"),
                SymptomOption("dizziness", "Dizziness"),
                SymptomOption("migraine", "Migraine")
            ),
            subParts = listOf(
                SubPart(
                    id = "sub_eye",
                    label = "Eye",
                    symptoms = listOf(
                        SymptomOption("eye_pain", "Eye pain"),
                        SymptomOption("blurred_vision", "Blurred vision"),
                        SymptomOption("eye_redness", "Eye redness"),
                        SymptomOption("watery_eyes", "Watery eyes")
                    )
                ),
                SubPart(
                    id = "sub_nose",
                    label = "Nose",
                    symptoms = listOf(
                        SymptomOption("runny_nose", "Runny nose"),
                        SymptomOption("nasal_congestion", "Nasal congestion"),
                        SymptomOption("nosebleed", "Nosebleed"),
                        SymptomOption("loss_of_smell", "Loss of smell")
                    )
                ),
                SubPart(
                    id = "sub_ear",
                    label = "Ear",
                    symptoms = listOf(
                        SymptomOption("ear_pain", "Ear pain"),
                        SymptomOption("hearing_loss", "Hearing loss"),
                        SymptomOption("ringing_in_ears", "Ringing in ears"),
                        SymptomOption("ear_discharge", "Ear discharge")
                    )
                ),
                SubPart(
                    id = "sub_mouth",
                    label = "Mouth",
                    symptoms = listOf(
                        SymptomOption("toothache", "Toothache"),
                        SymptomOption("sore_throat", "Sore throat"),
                        SymptomOption("mouth_ulcer", "Mouth ulcer"),
                        SymptomOption("difficulty_swallowing", "Difficulty swallowing")
                    )
                )
            )
        ),

        // ── NECK ──
        "body_region_neck" to BodyRegionData(
            regionId = "body_region_neck",
            label = "Neck",
            symptoms = listOf(
                SymptomOption("neck_pain", "Neck pain"),
                SymptomOption("stiff_neck", "Stiff neck"),
                SymptomOption("neck_swelling", "Neck swelling"),
                SymptomOption("difficulty_turning_head", "Difficulty turning head")
            )
        ),

        // ── CHEST ──
        "body_region_chest" to BodyRegionData(
            regionId = "body_region_chest",
            label = "Chest",
            symptoms = listOf(
                SymptomOption("chest_pain", "Chest pain"),
                SymptomOption("heart_palpitations", "Heart palpitations"),
                SymptomOption("shortness_of_breath", "Shortness of breath"),
                SymptomOption("chest_tightness", "Chest tightness"),
                SymptomOption("cough", "Cough")
            )
        ),

        // ── UPPER ABDOMEN ──
        "body_region_upper_abdomen" to BodyRegionData(
            regionId = "body_region_upper_abdomen",
            label = "Upper Abdomen",
            symptoms = listOf(
                SymptomOption("stomach_pain", "Stomach pain"),
                SymptomOption("nausea", "Nausea"),
                SymptomOption("vomiting", "Vomiting"),
                SymptomOption("bloating", "Bloating"),
                SymptomOption("acid_reflux", "Acid reflux")
            )
        ),

        // ── LOWER ABDOMEN ──
        "body_region_lower_abdomen" to BodyRegionData(
            regionId = "body_region_lower_abdomen",
            label = "Lower Abdomen",
            symptoms = listOf(
                SymptomOption("lower_abdominal_pain", "Lower abdominal pain"),
                SymptomOption("cramping", "Cramping"),
                SymptomOption("constipation", "Constipation"),
                SymptomOption("diarrhea", "Diarrhea"),
                SymptomOption("bloating_lower", "Bloating")
            )
        ),

        // ── PELVIS ──
        "body_region_pelvis" to BodyRegionData(
            regionId = "body_region_pelvis",
            label = "Pelvis",
            symptoms = listOf(
                SymptomOption("pelvic_pain", "Pelvic pain"),
                SymptomOption("hip_pain", "Hip pain"),
                SymptomOption("groin_pain", "Groin pain"),
                SymptomOption("urinary_issues", "Urinary issues")
            )
        ),

        // ── GENITAL AREA ──
        "body_region_genital_area" to BodyRegionData(
            regionId = "body_region_genital_area",
            label = "Genital Area",
            symptoms = listOf(
                SymptomOption("genital_pain", "Genital pain"),
                SymptomOption("itching", "Itching"),
                SymptomOption("discharge", "Discharge"),
                SymptomOption("swelling_genital", "Swelling")
            )
        ),

        // ── LEFT UPPER ARM ──
        "body_region_left_upper_arm" to BodyRegionData(
            regionId = "body_region_left_upper_arm",
            label = "Left Upper Arm",
            symptoms = listOf(
                SymptomOption("left_upper_arm_pain", "Pain in left upper arm"),
                SymptomOption("left_arm_weakness", "Weakness in left arm"),
                SymptomOption("left_arm_swelling", "Swelling in left arm"),
                SymptomOption("left_arm_numbness", "Numbness in left arm")
            )
        ),

        // ── LEFT FOREARM ──
        "body_region_left_forearm" to BodyRegionData(
            regionId = "body_region_left_forearm",
            label = "Left Forearm",
            symptoms = listOf(
                SymptomOption("left_forearm_pain", "Pain in left forearm"),
                SymptomOption("left_forearm_swelling", "Swelling in left forearm"),
                SymptomOption("left_wrist_pain", "Left wrist pain"),
                SymptomOption("left_forearm_numbness", "Numbness in left forearm")
            )
        ),

        // ── LEFT HAND ──
        "body_region_left_hand" to BodyRegionData(
            regionId = "body_region_left_hand",
            label = "Left Hand",
            symptoms = listOf(
                SymptomOption("left_hand_pain", "Pain in left hand"),
                SymptomOption("left_hand_swelling", "Swelling in left hand"),
                SymptomOption("left_finger_numbness", "Numbness in left fingers"),
                SymptomOption("left_hand_stiffness", "Stiffness in left hand")
            )
        ),

        // ── RIGHT UPPER ARM ──
        "body_region_right_upper_arm" to BodyRegionData(
            regionId = "body_region_right_upper_arm",
            label = "Right Upper Arm",
            symptoms = listOf(
                SymptomOption("right_upper_arm_pain", "Pain in right upper arm"),
                SymptomOption("right_arm_weakness", "Weakness in right arm"),
                SymptomOption("right_arm_swelling", "Swelling in right arm"),
                SymptomOption("right_arm_numbness", "Numbness in right arm")
            )
        ),

        // ── RIGHT FOREARM ──
        "body_region_right_forearm" to BodyRegionData(
            regionId = "body_region_right_forearm",
            label = "Right Forearm",
            symptoms = listOf(
                SymptomOption("right_forearm_pain", "Pain in right forearm"),
                SymptomOption("right_forearm_swelling", "Swelling in right forearm"),
                SymptomOption("right_wrist_pain", "Right wrist pain"),
                SymptomOption("right_forearm_numbness", "Numbness in right forearm")
            )
        ),

        // ── RIGHT HAND ──
        "body_region_right_hand" to BodyRegionData(
            regionId = "body_region_right_hand",
            label = "Right Hand",
            symptoms = listOf(
                SymptomOption("right_hand_pain", "Pain in right hand"),
                SymptomOption("right_hand_swelling", "Swelling in right hand"),
                SymptomOption("right_finger_numbness", "Numbness in right fingers"),
                SymptomOption("right_hand_stiffness", "Stiffness in right hand")
            )
        ),

        // ── LEFT THIGH ──
        "body_region_left_thigh" to BodyRegionData(
            regionId = "body_region_left_thigh",
            label = "Left Thigh",
            symptoms = listOf(
                SymptomOption("left_thigh_pain", "Pain in left thigh"),
                SymptomOption("left_thigh_swelling", "Swelling in left thigh"),
                SymptomOption("left_thigh_cramp", "Cramp in left thigh"),
                SymptomOption("left_thigh_numbness", "Numbness in left thigh")
            )
        ),

        // ── LEFT KNEE ──
        "body_region_left_knee" to BodyRegionData(
            regionId = "body_region_left_knee",
            label = "Left Knee",
            symptoms = listOf(
                SymptomOption("left_knee_pain", "Left knee pain"),
                SymptomOption("left_knee_swelling", "Swelling in left knee"),
                SymptomOption("left_knee_stiffness", "Stiffness in left knee"),
                SymptomOption("left_knee_locking", "Knee locking")
            )
        ),

        // ── LEFT LOWER LEG ──
        "body_region_left_lower_leg" to BodyRegionData(
            regionId = "body_region_left_lower_leg",
            label = "Left Lower Leg",
            symptoms = listOf(
                SymptomOption("left_shin_pain", "Left shin pain"),
                SymptomOption("left_calf_pain", "Left calf pain"),
                SymptomOption("left_leg_swelling", "Swelling in left leg"),
                SymptomOption("left_leg_cramp", "Cramp in left leg")
            )
        ),

        // ── LEFT FOOT ──
        "body_region_left_foot" to BodyRegionData(
            regionId = "body_region_left_foot",
            label = "Left Foot",
            symptoms = listOf(
                SymptomOption("left_foot_pain", "Pain in left foot"),
                SymptomOption("left_ankle_pain", "Left ankle pain"),
                SymptomOption("left_foot_swelling", "Swelling in left foot"),
                SymptomOption("left_foot_numbness", "Numbness in left foot")
            )
        ),

        // ── RIGHT THIGH ──
        "body_region_right_thigh" to BodyRegionData(
            regionId = "body_region_right_thigh",
            label = "Right Thigh",
            symptoms = listOf(
                SymptomOption("right_thigh_pain", "Pain in right thigh"),
                SymptomOption("right_thigh_swelling", "Swelling in right thigh"),
                SymptomOption("right_thigh_cramp", "Cramp in right thigh"),
                SymptomOption("right_thigh_numbness", "Numbness in right thigh")
            )
        ),

        // ── RIGHT KNEE ──
        "body_region_right_knee" to BodyRegionData(
            regionId = "body_region_right_knee",
            label = "Right Knee",
            symptoms = listOf(
                SymptomOption("right_knee_pain", "Right knee pain"),
                SymptomOption("right_knee_swelling", "Swelling in right knee"),
                SymptomOption("right_knee_stiffness", "Stiffness in right knee"),
                SymptomOption("right_knee_locking", "Knee locking")
            )
        ),

        // ── RIGHT LOWER LEG ──
        "body_region_right_lower_leg" to BodyRegionData(
            regionId = "body_region_right_lower_leg",
            label = "Right Lower Leg",
            symptoms = listOf(
                SymptomOption("right_shin_pain", "Right shin pain"),
                SymptomOption("right_calf_pain", "Right calf pain"),
                SymptomOption("right_leg_swelling", "Swelling in right leg"),
                SymptomOption("right_leg_cramp", "Cramp in right leg")
            )
        ),

        // ── RIGHT FOOT ──
        "body_region_right_foot" to BodyRegionData(
            regionId = "body_region_right_foot",
            label = "Right Foot",
            symptoms = listOf(
                SymptomOption("right_foot_pain", "Pain in right foot"),
                SymptomOption("right_ankle_pain", "Right ankle pain"),
                SymptomOption("right_foot_swelling", "Swelling in right foot"),
                SymptomOption("right_foot_numbness", "Numbness in right foot")
            )
        )
    )

    fun getRegionData(regionId: String): BodyRegionData? = regionDataMap[regionId]

    fun getDisplayLabel(regionId: String): String =
        regionDataMap[regionId]?.label ?: regionId.removePrefix("body_region_").replace("_", " ")
            .replaceFirstChar { it.uppercase() }
}
