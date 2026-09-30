package com.example.movieapp.feature.auth.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.R
import com.example.movieapp.feature.auth.AuthEnum
import com.example.movieapp.feature.auth.AuthIntent
import com.example.movieapp.feature.auth.AuthUiState
import com.example.movieapp.feature.auth.AuthViewModel
import com.example.movieapp.ui.components.AuthLayout
import com.example.movieapp.ui.components.AuthTextField
import com.example.movieapp.ui.components.MovieButton
import com.example.movieapp.ui.components.SocialSignInRow
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.PasswordToggle
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.TextMuted
import com.example.movieapp.ui.theme.TextStyles
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = koinViewModel(
        parameters = {
            parametersOf(AuthEnum.SignUp)
        },
    ),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SignUpContent(
        state = state,
        onIntent = { intent ->
            viewModel.onIntent(intent)
        },
        modifier = modifier,
    )
}

@Composable
private fun SignUpContent(
    state: AuthUiState,
    onIntent: (AuthIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var passVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    val signup: () -> Unit = {
        if (!state.button.isLoading) {
            focusManager.clearFocus()
            onIntent(AuthIntent.AuthClicked)
        }
    }

    AuthLayout(
        title = stringResource(R.string.signupTitle),
        onSkipClick = {
            if (!state.button.isLoading) {
                focusManager.clearFocus()
                onIntent(AuthIntent.SkipClicked)
            }
        },
        footer = {
            SignInFooter(
                enabled = !state.button.isLoading,
                onSignInClick = {
                    focusManager.clearFocus()
                    onIntent(AuthIntent.SignInClicked)
                },
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AuthTextField(
                value = state.email.input,
                onValueChange = {
                    onIntent(AuthIntent.EmailChanged(it))
                },
                placeholder = stringResource(R.string.emailPlace),
                enabled = !state.button.isLoading,
                errorMsg = state.email.errorMsg?.let { errorId ->
                    stringResource(errorId)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                ),
            )

            Spacer(Modifier.height(9.dp))

            AuthTextField(
                value = state.password.input,
                onValueChange = {
                    onIntent(AuthIntent.PasswordChanged(it))
                },
                placeholder = stringResource(R.string.passPlace),
                enabled = !state.button.isLoading,
                errorMsg = state.password.errorMsg?.let { errorId ->
                    stringResource(errorId)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        signup()
                    },
                ),
                visualTransformation = if (passVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passVisible = !passVisible
                        },
                        enabled = !state.button.isLoading,
                    ) {
                        Icon(
                            imageVector = if (passVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = stringResource(
                                if (passVisible) {
                                    R.string.hidePass
                                } else {
                                    R.string.showPass
                                }
                            ),
                            tint = PasswordToggle,
                        )
                    }
                },
            )

            Spacer(Modifier.height(28.dp))

            state.AuthError?.let { errorId ->
                Text(
                    text = stringResource(errorId),
                    style = TextStyles.Terms,
                    color = RedPrime,
                )

                Spacer(Modifier.height(12.dp))
            }

            MovieButton(
                text = stringResource(
                    if (state.button.isLoading)
                    {
                        R.string.signingUp
                    }
                    else
                    {
                        R.string.signupBtn
                    }
                ),
                onClick = signup,
                enabled = !state.button.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(12.dp))

            val terms = stringResource(R.string.signupTerms)
            val privacyPolicy = stringResource(R.string.privacyPolicy)

            Text(
                text = buildAnnotatedString {
                    append(terms)
                    append(" ")

                    withStyle(
                        style = SpanStyle(color = MovieWhite),
                    )
                    {
                        append(privacyPolicy)
                    }
                },
                style = TextStyles.Terms,
                color = TextMuted,
            )

            Spacer(Modifier.height(28.dp))

            SocialSignInRow(
                facebookIcon = R.drawable.facebook_icon,
                googleIcon = R.drawable.google_icon,
                appleIcon = R.drawable.apple_icon,
                onFacebookClick = {
                    onIntent(AuthIntent.FacebookClicked)
                },
                onGoogleClick = {
                    onIntent(AuthIntent.GoogleClicked)
                },
                onAppleClick = {
                    onIntent(AuthIntent.AppleClicked)
                },
                enabled = !state.button.isLoading,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SignInFooter(
    enabled: Boolean,
    onSignInClick: () -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
            Alignment.CenterHorizontally,
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(R.string.accountAlready),
            style = TextStyles.AuthBody,
            color = TextMuted,
        )

        Text(
            text = stringResource(R.string.signInTitle),
            style = TextStyles.AuthBody,
            color = MovieWhite,
            modifier = Modifier.clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onSignInClick,
            ),
        )
    }
}

