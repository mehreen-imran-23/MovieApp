package com.example.movieapp.feature.auth

import androidx.annotation.StringRes

data class AuthUiState(
    val email: InputFieldState = InputFieldState(),
    val password: InputFieldState = InputFieldState(),
    val button: AuthButtonState = AuthButtonState(),
    @StringRes val successMsg: Int? = null,
    @StringRes val AuthError: Int? = null,
)