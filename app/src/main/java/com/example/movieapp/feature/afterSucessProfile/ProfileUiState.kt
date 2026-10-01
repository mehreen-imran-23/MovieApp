package com.example.movieapp.feature.afterSucessProfile

import androidx.annotation.StringRes
import com.example.movieapp.feature.auth.InputFieldState

data class ProfileUiState (
    val name: InputFieldState = InputFieldState(),
    val phoneNumber: InputFieldState = InputFieldState(),
    val city: InputFieldState = InputFieldState(),
    val isLoading: Boolean = false,
    @param:StringRes val errorMsg: Int? = null,
)
