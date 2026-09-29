package com.example.movieapp.feature.onboarding

sealed interface OnboardingIntent {

    data object Next : OnboardingIntent
    data object Skip : OnboardingIntent

    data class GenreClicked(
        val genreId: Int,
    ) : OnboardingIntent
}