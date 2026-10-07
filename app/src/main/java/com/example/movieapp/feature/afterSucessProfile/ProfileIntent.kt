package com.example.movieapp.feature.afterSucessProfile

sealed interface ProfileIntent {
    data class NameChanged(val name: String) : ProfileIntent
    data class PhoneNumber(val phoneNumber: String) : ProfileIntent
    data class City(val city: String) : ProfileIntent
    data object ContinueClicked : ProfileIntent
}