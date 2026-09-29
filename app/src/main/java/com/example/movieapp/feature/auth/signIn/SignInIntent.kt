package com.example.movieapp.feature.auth.signin

sealed interface SignInIntent {

    data class EmailChanged(
        val email: String,
    ) : SignInIntent

    data class PasswordChanged(
        val password: String,
    ) : SignInIntent

    data object SignInClicked : SignInIntent
    data object ForgotPasswordClicked : SignInIntent
    data object SignUpClicked : SignInIntent
    data object SkipClicked : SignInIntent
    data object GoogleClicked : SignInIntent
    data object FacebookClicked : SignInIntent
    data object AppleClicked : SignInIntent
}