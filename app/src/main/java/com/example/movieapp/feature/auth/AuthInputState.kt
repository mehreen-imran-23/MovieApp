package com.example.movieapp.feature.auth

import androidx.annotation.StringRes

data class InputFieldState(
    val input: String = "",
    @StringRes val errorMsg: Int? = null,
)