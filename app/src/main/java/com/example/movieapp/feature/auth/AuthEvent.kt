package com.example.movieapp.feature.auth

sealed interface AuthEvent {
    data object NavigateToHome : AuthEvent
    data object NavigateToSignIn : AuthEvent
    data object NavigateToSignUp : AuthEvent
    data object NavigateToProfileDetails : AuthEvent
    data object NavigateToForgotPass : AuthEvent
    data object OpenPrivacyPolicy : AuthEvent
    data object GoogleAuth : AuthEvent
    data object FacebookAuth : AuthEvent
    data object AppleAuth : AuthEvent
}