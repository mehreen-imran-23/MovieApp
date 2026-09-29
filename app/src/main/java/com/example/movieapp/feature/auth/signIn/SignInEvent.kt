package com.example.movieapp.feature.auth.signin

sealed interface SignInEvent {

    data object NavigateToHome : SignInEvent

    data object NavigateToSignUp : SignInEvent

    data object NavigateToForgotPass : SignInEvent

    data object GoogleSignIn : SignInEvent

    data object FacebookSignIn : SignInEvent

    data object AppleSignIn : SignInEvent
}