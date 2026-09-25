package com.example.movieapp.feature.onboarding

sealed interface Onboarding1Event {
    data object NavigateToGenres : Onboarding1Event
    data object NavigateToSignIn : Onboarding1Event
}