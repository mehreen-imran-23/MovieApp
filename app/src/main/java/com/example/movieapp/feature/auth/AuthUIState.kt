package com.example.movieapp.feature.auth
import androidx.annotation.StringRes

data class AuthState(
    val email: InputFieldState = InputFieldState(),
    val password: InputFieldState = InputFieldState(),
    val button: AuthButtonState = AuthButtonState(),
    @StringRes val successMsg: Int? = null,
    @StringRes val AuthError: Int? = null,
)