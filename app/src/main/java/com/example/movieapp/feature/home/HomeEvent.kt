package com.example.movieapp.feature.home

sealed interface HomeEvent {
    data object NavigateToSearch : HomeEvent
    data class NavigateToMovieDetails(val movieId: Int) : HomeEvent
    data object NavigateToSignIn : HomeEvent
    data object LogoutFailed : HomeEvent
}