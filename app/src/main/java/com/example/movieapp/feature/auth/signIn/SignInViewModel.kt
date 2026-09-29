package com.example.movieapp.feature.auth.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SignInState())
    val uiState: StateFlow<SignInState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SignInEvent>(
        replay = 0,
    )
    val events: SharedFlow<SignInEvent> = _events.asSharedFlow()

    fun onIntent(intent: SignInIntent) {
        when (intent) {
            is SignInIntent.EmailChanged -> {
                _uiState.update { state ->
                    state.copy(
                        email = intent.email,
                        emailError = null,
                        signInError = null,
                    )
                }
            }

            is SignInIntent.PasswordChanged -> {
                _uiState.update { state ->
                    state.copy(
                        password = intent.password,
                        passwordError = null,
                        signInError = null,
                    )
                }
            }

            SignInIntent.SignInClicked -> {
                validateAndSignIn()
            }

            SignInIntent.ForgotPasswordClicked -> {
                sendEvent(SignInEvent.NavigateToForgotPass)
            }

            SignInIntent.SignUpClicked -> {
                sendEvent(SignInEvent.NavigateToSignUp)
            }

            SignInIntent.SkipClicked -> {
                sendEvent(SignInEvent.NavigateToHome)
            }

            SignInIntent.GoogleClicked -> {
                sendEvent(SignInEvent.GoogleSignIn)
            }

            SignInIntent.FacebookClicked -> {
                sendEvent(SignInEvent.FacebookSignIn)
            }

            SignInIntent.AppleClicked -> {
                sendEvent(SignInEvent.AppleSignIn)
            }
        }
    }

    private fun validateAndSignIn() {
        val state = _uiState.value

        if (state.isLoading) return

        val email = state.email.trim()

        val emailError = when {
            email.isEmpty() -> R.string.EmailReq

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                R.string.InvalidEmail

            else -> null
        }

        val passwordError = if (state.password.isEmpty()) {
            R.string.PassReq
        } else {
            null
        }

        _uiState.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                signInError = null,
            )
        }

        if (emailError != null || passwordError != null)
            return
    }

    private fun sendEvent(event: SignInEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}