package com.example.movieapp.feature.search

sealed interface SearchEvent {
    data object NavigateBack : SearchEvent
    data class NavigateToMovieDetails(val movieId: Int) : SearchEvent
}