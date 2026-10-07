package com.example.movieapp.feature.afterSucessProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.repository.ProfileRepository
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

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProfileEvent>(
        replay = 0,
    )
    val events = _events.asSharedFlow()

    fun onIntent(intent: ProfileIntent) {
        if (_uiState.value.isLoading) {
            return
        }

        when (intent) {
            is ProfileIntent.NameChanged -> {
                _uiState.update { state ->
                    state.copy(
                        name = state.name.copy(
                            input = intent.name,
                            errorMsg = null,
                        ),
                        errorMsg = null,
                    )
                }
            }

            is ProfileIntent.PhoneNumber -> {
                _uiState.update { state ->
                    state.copy(
                        phoneNumber = state.phoneNumber.copy(
                            input = intent.phoneNumber,
                            errorMsg = null,
                        ),
                        errorMsg = null,
                    )
                }
            }

            is ProfileIntent.City -> {
                _uiState.update { state ->
                    state.copy(
                        city = state.city.copy(
                            input = intent.city,
                            errorMsg = null,
                        ),
                        errorMsg = null,
                    )
                }
            }

            ProfileIntent.ContinueClicked -> {
                saveProfile()
            }
        }
    }

    private fun saveProfile() {
        val state = _uiState.value

        val name = state.name.input.trim()
        val phoneNumber = state.phoneNumber.input.trim()
        val city = state.city.input.trim()

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
        } else {
            null
        }

        val isValid = nameError == null && phoneError == null && cityError == null

        _uiState.update { current ->
            current.copy(
                name = current.name.copy(
                    input = name,
                    errorMsg = nameError,
                ),
                phoneNumber = current.phoneNumber.copy(
                    input = phoneNumber,
                    errorMsg = phoneError,
                ),
                city = current.city.copy(
                    input = city,
                    errorMsg = cityError,
                ),
                isLoading = isValid,
                errorMsg = null,
            )
        }

        if (!isValid) {
            return
        }

        viewModelScope.launch {
            try {
                val userId = preferences.activeUserId.first()

                if (userId == null) {
                    _uiState.update { current ->
                        current.copy(
                            errorMsg = R.string.session_missing,
                        )
                    }
                    return@launch //dont call repo, stop
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
                    _uiState.update { current ->
                        current.copy(
                            errorMsg = R.string.save_failed,
                        )
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { current ->
                    current.copy(
                        errorMsg = R.string.save_failed,
                    )
                }
            } finally {
                _uiState.update { current ->
                    current.copy(isLoading = false)
                }
            }
        }
    }
}