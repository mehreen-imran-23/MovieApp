package com.example.movieapp.feature.onboarding

data class OnboardingGenre(
    val id: Int,
    val name: String,
)

data class OnboardingState(
    val posterUrls: List<String> = emptyList(),
    val genres: List<OnboardingGenre> = emptyList(),
    val selectedGenre: Set<Int> = emptySet(),
    val isLoading: Boolean = true,
    val errorMsg: String? = null,
)