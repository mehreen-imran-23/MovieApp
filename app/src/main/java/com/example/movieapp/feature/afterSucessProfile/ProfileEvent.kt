package com.example.movieapp.feature.profile

sealed interface ProfileEvent {
    data object NavigateToHome : ProfileEvent
}