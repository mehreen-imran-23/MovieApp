package com.example.movieapp.feature.splash

sealed interface SplashEvent {
    data object Navigate : SplashEvent
}