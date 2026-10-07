package com.example.movieapp.feature.home

sealed interface HomeIntent {

    data object SearchClicked : HomeIntent
    data class BookClicked(val movieId: Int) : HomeIntent
}