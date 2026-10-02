package com.example.movieapp.feature.auth

import androidx.compose.remote.creation.dsl.first
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.repository.AuthRepository
import com.example.movieapp.data.repository.AuthResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val mode: AuthEnum,
    private val repository: AuthRepository,
    private val preferences: LocalPreferences,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<AuthEvent>(replay = 0)
    val events: SharedFlow<AuthEvent> = _events.asSharedFlow()
    private val emailRegex = Regex("""^[A-Za-z0-9]+(?:[._%+-][A-Za-z0-9]+)*@[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*(?:\.[A-Za-z]{2,})+$""")
    private val passwordRegex = Regex("""^(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9\s]).{8,}$""")
    fun onIntent(intent: AuthIntent)
    {
        if (_uiState.value.button.isLoading)
        {
            return
        }

        when (intent)
        {
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

            AuthIntent.SignUpClicked -> {
                clearForm()
                sendEvent(AuthEvent.NavigateToSignUp)
            }

            AuthIntent.SignInClicked -> {
                clearForm()
                sendEvent(AuthEvent.NavigateToSignIn)
            }

            AuthIntent.SkipClicked ->
                continueAsGuest()

            AuthIntent.GoogleClicked ->
                sendEvent(AuthEvent.GoogleAuth)

            AuthIntent.FacebookClicked ->
                sendEvent(AuthEvent.FacebookAuth)

            AuthIntent.AppleClicked ->
                sendEvent(AuthEvent.AppleAuth)
        }
    }

    private fun validateAndSignIn() {
        if (!validateInput()) {
            return
        }
        authenticate()
    }

    private fun validateAndSignUp() {
        if (!validateInput())
        {
            return
        }
        authenticate()
    }

    private fun authenticate() {
        val email = _uiState.value.email.input
        val password = _uiState.value.password.input

        _uiState.update { state ->
            state.copy(
                button = state.button.copy(isLoading = true),
                AuthError = null,
                successMsg = null,
            )
        }

        viewModelScope.launch {
            try {
                val result = when (mode) {
                    AuthEnum.SignIn -> repository.signIn(
                        email = email,
                        password = password,

                        )

                        AuthEnum.SignUp -> repository.signUp(
                        email = email,
                        password = password,
                        selectedGenreIds = preferences.selectedGenreIds.first(),
                    )
                }

                when (result)
                {
                    is AuthResult.Success -> {
                            preferences.saveUserSession(result.userId)

                        _uiState.update { state ->
                            state.copy(
                                email = InputFieldState(),
                                password = InputFieldState(),
                                successMsg = when (mode)
                                {
                                    AuthEnum.SignIn ->
                                        R.string.signin_success
                                    AuthEnum.SignUp ->
                                        R.string.signup_success
                                },
                            )
                        }

                        val event = when (mode)
                        {
                            AuthEnum.SignIn ->
                                AuthEvent.NavigateToHome

                            AuthEnum.SignUp ->
                                AuthEvent.NavigateToProfile
                        }

                        if (mode == AuthEnum.SignUp) {
                            clearForm()
                        }

                        _events.emit(event)
                    }

                    AuthResult.EmailAlreadyExists -> {
                        _uiState.update { state ->
                            state.copy(
                                AuthError = R.string.email_already_exists,
                            )
                        }
                    }

                    AuthResult.InvalidCredentials -> {
                        _uiState.update { state ->
                            state.copy(
                                AuthError = R.string.invaldi_credentials,
                            )
                        }
                    }
                }
            }
            catch (exception: CancellationException)
            {
                throw exception
            }
            catch (exception: Exception)
            {
                _uiState.update { state ->
                    state.copy(
                        AuthError = R.string.auth_failed,
                    )
                }
            }
            finally
            {
                _uiState.update { state ->
                    state.copy(
                        button = state.button.copy(isLoading = false),
                    )
                }
            }
        }
    }

    private fun continueAsGuest() {
        _uiState.update { state ->
            state.copy(
                button = state.button.copy(isLoading = true),
                AuthError = null,
                successMsg = null,
            )
        }

        viewModelScope.launch {
            try {
                preferences.saveGuest()
                _events.emit(AuthEvent.NavigateToHome)
            }
            catch (exception: CancellationException)
            {
                throw exception
            }
            catch (exception: Exception)
            {
                _uiState.update { state ->
                    state.copy(
                        AuthError = R.string.auth_failed,
                    )
                }
            }
            finally
            {
                _uiState.update { state ->
                    state.copy(
                        button = state.button.copy(isLoading = false),
                    )
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        val state = _uiState.value
        val email = state.email.input.trim()
        val password = state.password.input

        val emailError = when
        {
            email.isEmpty() -> R.string.email_req
            !emailRegex.matches(email) -> R.string.invalid_email
            else -> null
        }

        val passError = when
        {
            password.isEmpty() -> R.string.pass_req

            mode == AuthEnum.SignIn && password.length < 8 ->
                R.string.invalid_password

            mode == AuthEnum.SignUp &&
                    !passwordRegex.matches(password) ->
                R.string.valid_pass

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

    private fun clearForm() {
        _uiState.value = AuthUiState()
    }
}