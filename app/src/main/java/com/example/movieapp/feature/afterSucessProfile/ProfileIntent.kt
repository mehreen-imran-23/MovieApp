package com.example.movieapp.feature.afterSucessProfile

sealed interface ProfileIntent {
    data class ContinueClicked(
        val name: String,
        val phoneNumber: String,
        val city: String,
    ) : ProfileIntent
}