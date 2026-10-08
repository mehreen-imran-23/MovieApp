package com.example.movieapp.feature.moviedetails

sealed interface MovieDetailsEvent {
    data object NavigateBack : MovieDetailsEvent
}