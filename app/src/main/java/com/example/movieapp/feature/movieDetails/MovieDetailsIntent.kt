package com.example.movieapp.feature.movieDetails

sealed interface MovieDetailsIntent {
    data object BackClicked : MovieDetailsIntent
}