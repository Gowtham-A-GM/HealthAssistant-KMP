package com.example.healthassistant.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.core.utils.t
import com.example.healthassistant.designsystem.AppColors
import com.example.healthassistant.designsystem.AppTypography

private fun isValidEmailSignup(email: String): Boolean {
    val pattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return pattern.matches(email.trim())
}

@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onSignupSuccess: () -> Unit,
    onBack: () -> Unit = {}
) {
    val state = viewModel.state.value
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var confirmTouched by remember { mutableStateOf(false) }

    val emailError = if (emailTouched && state.email.isNotBlank() && !isValidEmailSignup(state.email))
        t("Please enter a valid email (e.g. name@example.com)") else null
    val passwordError = if (passwordTouched && state.password.isNotBlank() && state.password.length < 8)
        t("Password must be at least 8 characters.") else null
    val confirmError = if (confirmTouched && state.confirmPassword.isNotBlank() && state.password != state.confirmPassword)
        t("Passwords do not match.") else null

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            AppLogger.d("SIGNUP_SCREEN", "Navigating to Profile")
            onSignupSuccess()
            viewModel.resetSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = AppColors.textPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = t("Create your account"),
            style = AppTypography.h1(),
            color = AppColors.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = t("Sign up to get started with your health journey."),
            style = AppTypography.bodySmall(),
            color = AppColors.dustyGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = t("Email"),
            style = AppTypography.title(),
            color = AppColors.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = state.email,
            onValueChange = {
                emailTouched = true
                viewModel.onEvent(AuthEvent.OnEmailChange(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. name@example.com", style = AppTypography.bodySmall(), color = AppColors.dustyGray) },
            shape = RoundedCornerShape(14.dp),
            isError = emailError != null,
            supportingText = if (emailError != null) ({
                Text(emailError, style = AppTypography.bodySmall(), color = Color(0xFFD32F2F))
            }) else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (emailError != null) Color(0xFFD32F2F) else AppColors.darkBlue,
                unfocusedBorderColor = if (emailError != null) Color(0xFFD32F2F) else AppColors.dustyGray.copy(alpha = 0.4f),
                focusedTextColor = AppColors.textPrimary,
                unfocusedTextColor = AppColors.textPrimary,
                cursorColor = AppColors.darkBlue,
                errorBorderColor = Color(0xFFD32F2F),
                errorCursorColor = Color(0xFFD32F2F)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = t("Password"),
            style = AppTypography.title(),
            color = AppColors.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = state.password,
            onValueChange = {
                passwordTouched = true
                viewModel.onEvent(AuthEvent.OnPasswordChange(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(t("Enter your password"), style = AppTypography.bodySmall(), color = AppColors.dustyGray) },
            shape = RoundedCornerShape(14.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError != null,
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = AppColors.dustyGray
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (passwordError != null) Color(0xFFD32F2F) else AppColors.darkBlue,
                unfocusedBorderColor = if (passwordError != null) Color(0xFFD32F2F) else AppColors.dustyGray.copy(alpha = 0.4f),
                focusedTextColor = AppColors.textPrimary,
                unfocusedTextColor = AppColors.textPrimary,
                cursorColor = AppColors.darkBlue,
                errorBorderColor = Color(0xFFD32F2F),
                errorCursorColor = Color(0xFFD32F2F)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            supportingText = {
                Text(
                    text = passwordError ?: t("Password must be at least 8 characters."),
                    style = AppTypography.bodySmall(),
                    color = if (passwordError != null) Color(0xFFD32F2F) else AppColors.dustyGray
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = t("Confirm Password"),
            style = AppTypography.title(),
            color = AppColors.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = {
                confirmTouched = true
                viewModel.onEvent(AuthEvent.OnConfirmPasswordChange(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(t("Re-enter your password"), style = AppTypography.bodySmall(), color = AppColors.dustyGray) },
            shape = RoundedCornerShape(14.dp),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = confirmError != null,
            supportingText = if (confirmError != null) ({
                Text(confirmError, style = AppTypography.bodySmall(), color = Color(0xFFD32F2F))
            }) else null,
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = AppColors.dustyGray
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (confirmError != null) Color(0xFFD32F2F) else AppColors.darkBlue,
                unfocusedBorderColor = if (confirmError != null) Color(0xFFD32F2F) else AppColors.dustyGray.copy(alpha = 0.4f),
                focusedTextColor = AppColors.textPrimary,
                unfocusedTextColor = AppColors.textPrimary,
                cursorColor = AppColors.darkBlue,
                errorBorderColor = Color(0xFFD32F2F),
                errorCursorColor = Color(0xFFD32F2F)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        state.errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                style = AppTypography.bodySmall(),
                color = androidx.compose.ui.graphics.Color(0xFFD32F2F)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { viewModel.onEvent(AuthEvent.OnSignupClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.darkBlue)
        ) {
            Text(
                text = t("Sign up"),
                style = AppTypography.title(),
                color = AppColors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = t("Already have an account?"),
                style = AppTypography.bodySmall(),
                color = AppColors.dustyGray
            )
            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = t("Log in"),
                    style = AppTypography.bodySmall(),
                    color = AppColors.blue
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
