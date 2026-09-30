package com.example.movieapp.feature.auth.signin

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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.R
import com.example.movieapp.feature.auth.AuthEnum
import com.example.movieapp.feature.auth.AuthIntent
import com.example.movieapp.feature.auth.AuthUIState
import com.example.movieapp.feature.auth.AuthViewModel
import com.example.movieapp.ui.components.AuthLayout
import com.example.movieapp.ui.components.AuthTextField
import com.example.movieapp.ui.components.MovieButton
import com.example.movieapp.ui.components.SocialSignInRow
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.PasswordToggle
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.Text2
import com.example.movieapp.ui.theme.TextMuted
import com.example.movieapp.ui.theme.TextStyles
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun SignInScreen(
    viewModel: AuthViewModel = koinViewModel(
        parameters = { parametersOf(AuthEnum.SignIn) },
    ),

    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SignInContent(
        state = state,
        onIntent = { intent ->
            viewModel.onIntent(intent)
        },
        modifier = modifier,
    )
}


@Composable
private fun SignInContent(
    state: AuthUIState,
    onIntent: (AuthIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var passVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    val signin = {
        if (!state.isLoading) {
            focusManager.clearFocus()
            onIntent(AuthIntent.AuthClicked)
        }
    }

    AuthLayout(
        title = stringResource(R.string.signInTitle),
        onSkipClick = {
            if (!state.isLoading) {
                onIntent(AuthIntent.SkipClicked)
            }
        },
        footer = {
            SignUpFooter(
                enabled = !state.isLoading,
                onSignUpClick = {
                    onIntent(AuthIntent.SignUpClicked)
                },
            )
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {

            AuthTextField(
                value = state.email,
                onValueChange = {
                    onIntent(AuthIntent.EmailChanged(it))
                },
                placeholder = stringResource(R.string.emailPlace),
                enabled = !state.isLoading,
                errorMsg = state.emailError?.let {
                    stringResource(it)
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

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            AuthTextField(
                value = state.password,
                onValueChange = {
                    onIntent(AuthIntent.PasswordChanged(it))
                },
                placeholder = stringResource(R.string.passPlace),
                enabled = !state.isLoading,
                errorMsg = state.passwordError?.let {
                    stringResource(it)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        signin()
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
                        enabled = !state.isLoading,
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

            TextButton(
                onClick = {
                    onIntent(AuthIntent.ForgotPassClicked)
                },
                enabled = !state.isLoading,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(
                    text = stringResource(R.string.forgotPass),
                    style = TextStyles.AuthBody,
                    color = Text2,
                )
            }

            state.AuthError?.let { errorId ->
                Text(
                    text = stringResource(errorId),
                    style = TextStyles.Terms,
                    color = RedPrime,
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            MovieButton(
                text = stringResource(
                    if (state.isLoading) {
                        R.string.signingIn
                    } else {
                        R.string.signInButton
                    }
                ),
                onClick = signin,
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

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
                enabled = !state.isLoading,
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SignUpFooter(
    enabled: Boolean,
    onSignUpClick: () -> Unit,
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
            text = stringResource(R.string.haveAccount),
            style = TextStyles.AuthBody,
            color = TextMuted,
        )

        Text(
            text = stringResource(R.string.signupTitle),
            style = TextStyles.AuthBody,
            color = MovieWhite,
            modifier = Modifier.clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onSignUpClick,
            ),
        )
    }
}