package com.example.healthassistant.presentation.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var state = mutableStateOf(AuthState())
        private set

    fun onEvent(event: AuthEvent) {
        when (event) {

            is AuthEvent.OnEmailChange ->
                state.value = state.value.copy(email = event.email)

            is AuthEvent.OnPasswordChange ->
                state.value = state.value.copy(password = event.password)

            is AuthEvent.OnConfirmPasswordChange ->
                state.value = state.value.copy(confirmPassword = event.confirmPassword)

            AuthEvent.OnSignupClick ->
                signup()

            AuthEvent.OnLoginClick ->
                login()
        }
    }

    private fun signup() {

        if (state.value.password != state.value.confirmPassword) {
            state.value = state.value.copy(
                errorMessage = "Passwords do not match"
            )
            return
        }

        viewModelScope.launch {

            state.value = state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {

                // 1️⃣ CALL SIGNUP API
                val signupResponse = repository.signup(
                    state.value.email,
                    state.value.password
                )

                if (!signupResponse.success) {
                    state.value = state.value.copy(
                        isLoading = false,
                        errorMessage = signupResponse.message
                    )
                    return@launch
                }

                // 2️⃣ AUTO LOGIN AFTER SIGNUP
                val loginResponse = repository.login(
                    state.value.email,
                    state.value.password
                )

                if (loginResponse.success && loginResponse.token != null) {

                    AppLogger.d("AUTH_VM", "Auto Login Successful")
                    AppLogger.d("AUTH_VM", "Generated JWT Token → ${loginResponse.token}")

                    // 3️⃣ STORE TOKEN
                    state.value = state.value.copy(
                        token = loginResponse.token,
                        isSuccess = true
                    )

                } else {
                    state.value = state.value.copy(
                        errorMessage = loginResponse.message
                    )
                }

            } catch (e: Exception) {
                state.value = state.value.copy(
                    errorMessage = e.message ?: "Something went wrong"
                )
            }

            state.value = state.value.copy(isLoading = false)
        }
    }

    private fun login() {

        viewModelScope.launch {

            state.value = state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {

                val response = repository.login(
                    state.value.email,
                    state.value.password
                )

                if (response.success && response.token != null) {
                    state.value = state.value.copy(
                        token = response.token,
                        isSuccess = true
                    )
                } else {
                    state.value = state.value.copy(
                        errorMessage = response.message
                    )
                }

            } catch (e: Exception) {
                state.value = state.value.copy(
                    errorMessage = e.message ?: "Something went wrong"
                )
            }

            state.value = state.value.copy(isLoading = false)
        }
    }

    fun resetSuccess() {
        state.value = state.value.copy(isSuccess = false)
    }
}