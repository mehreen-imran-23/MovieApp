package com.example.movieapp.feature.auth.signin

import androidx.annotation.StringRes

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,

    @StringRes
    val emailError: Int? = null,

    @StringRes
    val passwordError: Int? = null,

    @StringRes
    val signInError: Int? = null,
)