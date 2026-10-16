package com.example.movieapp.feature.auth

sealed interface AuthIntent {

    data class EmailChanged(
        val email: String,
    ) : AuthIntent

    data class PasswordChanged(
        val password: String,
    ) : AuthIntent

    data object AuthClicked : AuthIntent

    data object ForgotPassClicked : AuthIntent
    data object SignUpClicked : AuthIntent
    data object SkipClicked : AuthIntent
    data object GoogleClicked : AuthIntent
    data object FacebookClicked : AuthIntent
    data object AppleClicked : AuthIntent

    data object SignInClicked : AuthIntent
}