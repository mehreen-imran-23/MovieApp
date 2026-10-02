package com.example.movieapp.feature.afterSucessProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.movieapp.R
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.repository.ProfileRepository
import com.example.movieapp.feature.auth.InputFieldState
import com.example.movieapp.feature.profile.ProfileEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val preferences: LocalPreferences,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProfileEvent>()
    val events = _events.asSharedFlow()

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.ContinueClicked -> saveProfile(intent)
        }
    }

    private fun saveProfile(intent: ProfileIntent.ContinueClicked) {
        if (_uiState.value.isLoading) return

        val name = intent.name.trim()
        val phoneNumber = intent.phoneNumber.trim()
        val city = intent.city.trim()

        val nameError = if (name.isBlank()) {
            R.string.name_required
        } else {
            null
        }

        val phoneError = if (phoneNumber.isBlank()) {
            R.string.phone_num
        } else {
            null
        }

        val cityError = if (city.isBlank()) {
            R.string.profile_city
        }
        else {
            null
        }

        _uiState.update { state ->
            state.copy(
                name = InputFieldState(
                    input = name,
                    errorMsg = nameError,
                ),
                phoneNumber = InputFieldState(
                    input = phoneNumber,
                    errorMsg = phoneError,
                ),
                city = InputFieldState(
                    input = city,
                    errorMsg = cityError,
                ),
                errorMsg = null,
            )
        }

        if (nameError != null || phoneError != null || cityError != null) {
            return
        }

        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        viewModelScope.launch {
            try {
                val userId = preferences.activeUserId.first()

                if (userId == null) {
                    _uiState.update { state ->
                        state.copy(errorMsg = R.string.session_missing)
                    }
                    return@launch
                }

                val saved = repository.saveProfile(
                    userId = userId,
                    name = name,
                    phoneNumber = phoneNumber,
                    city = city,
                )

                if (saved) {
                    _events.emit(ProfileEvent.NavigateToHome)
                } else {
                    _uiState.update { state ->
                        state.copy(errorMsg = R.string.save_failed)
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { state ->
                    state.copy(errorMsg = R.string.save_failed)
                }
            } finally {
                _uiState.update { state ->
                    state.copy(isLoading = false)
                }
            }
        }
    }
}