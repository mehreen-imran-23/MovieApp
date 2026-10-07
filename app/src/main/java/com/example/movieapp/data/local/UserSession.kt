package com.example.movieapp.data.local

sealed interface UserSession {
    data object SignedOut : UserSession
    data object Guest : UserSession
    data class LoggedIn(val userId: Long) : UserSession
}