package com.example.movieapp.feature.auth

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
data class InputFieldState(
    val input: String = "",
    @StringRes val errorMsg: Int? = null,
)