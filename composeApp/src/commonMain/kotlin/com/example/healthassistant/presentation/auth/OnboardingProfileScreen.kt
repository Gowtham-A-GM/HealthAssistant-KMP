package com.example.healthassistant.presentation.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import com.example.healthassistant.core.utils.t
import com.example.healthassistant.designsystem.AppColors
import com.example.healthassistant.designsystem.AppTypography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.healthassistant.core.image.ImagePickerManager
import com.example.healthassistant.core.utils.compressAndEncodeProfileImage
import com.example.healthassistant.presentation.auth.components.QuestionInput
import com.example.healthassistant.presentation.auth.questions.ProfileQuestionConfig
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import com.example.healthassistant.core.image.decodeBase64ToImageBitmap
import com.example.healthassistant.presentation.components.ErrorMessageCard
import com.example.healthassistant.presentation.components.OnboardingProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingProfileScreen(
    viewModel: OnboardingProfileViewModel,
    onProfileCompleted: () -> Unit
) {

    val state = viewModel.state.value

    val imagePicker = ImagePickerManager { bytes, mime ->

        val base64 = compressAndEncodeProfileImage(bytes)
        viewModel.updateProfileImage(base64)
    }

    val openGallery = imagePicker.rememberGalleryLauncher()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onProfileCompleted()
            viewModel.resetSuccess()
        }
    }

    val gender = state.answers["q_gender"]
    val age = state.answers["q_age"]?.toIntOrNull()
    val showFemaleQuestions =
        gender?.lowercase() == "female" &&
                age != null &&
                age in 12..55
    val allQuestions =
        ProfileQuestionConfig.questions +
                if (showFemaleQuestions)
                    ProfileQuestionConfig.femaleConditional
                else emptyList()


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {

            state.errorMessage?.let { error ->

                ErrorMessageCard(error)

                Spacer(Modifier.height(16.dp))
            }
        }

        item {

            OnboardingProgress(
                currentStep = 1,
                totalSteps = 2,
                title = "Complete Your Profile"
            )


            Spacer(Modifier.height(16.dp))
        }

        // -------------------------------
        // PROFILE IMAGE SECTION
        // -------------------------------

        item {

            Text(
                text = "Profile Photo",
                style = AppTypography.h3(),
                color = AppColors.textPrimary
            )

            Spacer(Modifier.height(12.dp))

            val profileImage = state.profileImageBase64?.let {
                decodeBase64ToImageBitmap(it)
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = AppColors.darkBlue,
                            shape = CircleShape
                        )
                        .clickable {
                            openGallery()
                        },
                    contentAlignment = Alignment.Center
                ) {

                    // avatar image
                    if (profileImage != null) {

                        Image(
                            bitmap = profileImage,
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(60.dp),
                            tint = AppColors.darkBlue
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tap to upload photo",
                    style = AppTypography.bodySmall(),
                    color = AppColors.dustyGray
                )
            }

            Spacer(Modifier.height(24.dp))
        }

        // -------------------------------
        // QUESTIONS FROM CONFIG
        // -------------------------------

        items(allQuestions) { question ->

            val currentValue =
                viewModel.getValueForQuestion(question.id)

            val showErrors = state.errorMessage != null

            QuestionInput(
                question = question.copy(value = currentValue),
                onValueChange = {
                    viewModel.onDynamicValueChange(question.id, it)
                },
                isRequiredError =
                    showErrors &&
                            question.required &&
                            currentValue.isBlank()
            )

            Spacer(Modifier.height(12.dp))
        }

        // -------------------------------
        // EMERGENCY CONTACTS
        // -------------------------------

        item {

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Emergency Contacts",
                style = AppTypography.h3(),
                color = AppColors.textPrimary
            )

            Spacer(Modifier.height(10.dp))

            state.emergencyContacts.forEachIndexed { index, contact ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "Emergency Contact ${index + 1}",
                            style = AppTypography.title(),
                            color = AppColors.textPrimary
                        )

                        Spacer(Modifier.height(8.dp))

                        Column {

                            OutlinedTextField(
                                value = contact.name,
                                onValueChange = {
                                    viewModel.updateContactName(index, it)
                                },
                                label = { Text("Contact Name") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(10.dp))

                            OutlinedTextField(
                                value = contact.number,
                                onValueChange = {
                                    viewModel.updateContactNumber(index, it)
                                },
                                label = { Text("Phone Number") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

            OutlinedButton(
                onClick = { viewModel.addEmergencyContact() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(t("+ Add Another Contact"), style = AppTypography.title(), color = AppColors.darkBlue)
            }

            Spacer(Modifier.height(20.dp))
        }

        // -------------------------------
        // SAVE BUTTON
        // -------------------------------

        item {

            Button(
                onClick = { viewModel.submitProfile() },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {

                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )

                } else {

                    Text(t("Save Profile"), style = AppTypography.title(), color = AppColors.textSecondary)
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }


}