package com.example.movieapp.feature.auth

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

class AuthViewModel(
    private val mode: AuthEnum,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>(replay = 0)
    val events: SharedFlow<AuthEvent> = _events.asSharedFlow()

    private val emailRegex = Regex("""^[A-Za-z0-9]+(?:[._%+-][A-Za-z0-9]+)*@[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*(?:\.[A-Za-z]{2,})+$""")
    private val passwordRegex = Regex("""^(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9\s]).{8,}$""")

    fun onIntent(intent: AuthIntent) {
        if (_uiState.value.button.isLoading) return

        when (intent) {
            is AuthIntent.EmailChanged -> {
                _uiState.update { state ->
                    state.copy(
                        email = state.email.copy(
                            input = intent.email,
                            errorMsg = null,
                        ),
                        AuthError = null,
                        successMsg = null,
                    )
                }
            }

            is AuthIntent.PasswordChanged -> {
                _uiState.update { state ->
                    state.copy(
                        password = state.password.copy(
                            input = intent.password,
                            errorMsg = null,
                        ),
                        AuthError = null,
                        successMsg = null,
                    )
                }
            }

            AuthIntent.AuthClicked -> {
                when (mode) {
                    AuthEnum.SignIn -> validateAndSignIn()
                    AuthEnum.SignUp -> validateAndSignUp()
                }
            }

            AuthIntent.ForgotPassClicked ->
                sendEvent(AuthEvent.NavigateToForgotPass)

            AuthIntent.SignUpClicked ->
                sendEvent(AuthEvent.NavigateToSignUp)

            AuthIntent.SignInClicked ->
                sendEvent(AuthEvent.NavigateToSignIn)

            AuthIntent.SkipClicked ->
                sendEvent(AuthEvent.NavigateToHome)

            AuthIntent.GoogleClicked ->
                sendEvent(AuthEvent.GoogleAuth)

            AuthIntent.FacebookClicked ->
                sendEvent(AuthEvent.FacebookAuth)

            AuthIntent.AppleClicked ->
                sendEvent(AuthEvent.AppleAuth)
        }
    }

    private fun validateAndSignIn() {
        if (!validateInput()) return
    }

    private fun validateAndSignUp() {
        if (!validateInput()) return
    }

    private fun validateInput(): Boolean {
        val state = _uiState.value
        val email = state.email.input.trim()
        val password = state.password.input

        val emailError = when {
            email.isEmpty() -> R.string.EmailReq
            !emailRegex.matches(email) -> R.string.InvalidEmail
            else -> null
        }

        val passError = when {
            password.isEmpty() -> R.string.PassReq

            mode == AuthEnum.SignIn && password.length < 8 ->
                R.string.InvalidPassword

            mode == AuthEnum.SignUp &&
                    !passwordRegex.matches(password) ->
                R.string.ValidPass

            else -> null
        }

        _uiState.update { currentState ->
            currentState.copy(
                email = currentState.email.copy(
                    input = email,
                    errorMsg = emailError,
                ),
                password = currentState.password.copy(
                    errorMsg = passError,
                ),
                AuthError = null,
                successMsg = null,
            )
        }

        return emailError == null && passError == null
    }

    private fun sendEvent(event: AuthEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}