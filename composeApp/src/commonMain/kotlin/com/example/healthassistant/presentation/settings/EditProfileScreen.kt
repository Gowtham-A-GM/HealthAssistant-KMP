package com.example.healthassistant.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.healthassistant.core.image.ImagePickerManager
import com.example.healthassistant.core.image.decodeBase64ToImageBitmap
import com.example.healthassistant.core.utils.encodeImageToBase64
import com.example.healthassistant.presentation.auth.components.QuestionInput
import com.example.healthassistant.presentation.auth.questions.ProfileQuestionConfig

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    onBack: () -> Unit
) {

    val state = viewModel.state.value
    LaunchedEffect(Unit) {
        viewModel.reload()
    }

    // IMAGE PICKER
    val imagePicker = ImagePickerManager { bytes, mime ->

        val base64 = encodeImageToBase64(bytes)

        viewModel.updateProfileImage(base64)
    }

    val openGallery = imagePicker.rememberGalleryLauncher()

    // NAVIGATION AFTER SAVE
    LaunchedEffect(state.isSuccess) {

        if (state.isSuccess) {
            onBack()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {

            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(20.dp))
        }

        // -------------------------------
        // PROFILE IMAGE
        // -------------------------------

        item {

            Text(
                text = "Profile Photo",
                style = MaterialTheme.typography.titleMedium
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
                            color = MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .clickable {
                            openGallery()
                        },
                    contentAlignment = Alignment.Center
                ) {

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
                            contentDescription = "Profile Icon",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.primary
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
                    text = "Tap to change photo",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(24.dp))
        }

        // -------------------------------
        // PROFILE QUESTIONS
        // -------------------------------

        items(ProfileQuestionConfig.questions) { question ->

            val value = remember(state.answers) {
                state.answers[question.id] ?: ""
            }

            QuestionInput(
                question = question.copy(value = value),
                onValueChange = {
                    viewModel.updateAnswer(question.id, it)
                },
                isRequiredError = false
            )

            Spacer(Modifier.height(12.dp))
        }

        // -------------------------------
        // SAVE BUTTON
        // -------------------------------

        item {

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { viewModel.saveProfile() },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {

                if (state.isLoading) {

                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )

                } else {

                    Text("Save Profile")
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}