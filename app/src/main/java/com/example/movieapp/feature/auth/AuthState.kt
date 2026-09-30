package com.example.movieapp.feature.auth

import androidx.annotation.StringRes

data class AuthState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    @StringRes
    val emailError: Int? = null,

    @StringRes
    val passError: Int? = null,

    @StringRes
    val AuthError: Int? = null,
)