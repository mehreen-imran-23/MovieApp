package com.example.movieapp.feature.splash

sealed interface SplashEvent {
    data object Navigate : SplashEvent
    data object NavigateToSignIn : SplashEvent
    data object NavigateToHome : SplashEvent
    data object NavigateToProfile : SplashEvent
}