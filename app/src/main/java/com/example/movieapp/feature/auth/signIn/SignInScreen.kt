package com.example.movieapp.feature.auth.signin

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.R
import com.example.movieapp.ui.components.AuthLayout
import com.example.movieapp.ui.components.AuthTextField
import com.example.movieapp.ui.components.MovieButton
import com.example.movieapp.ui.components.SocialSignInRow
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.PasswordToggle
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.Text2
import com.example.movieapp.ui.theme.TextStyles
import org.koin.androidx.compose.koinViewModel


@Composable
fun SignInScreen(
    viewModel: SignInViewModel = koinViewModel(),
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
    state: SignInState,
    onIntent: (SignInIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var passVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    val signin = {
        if (!state.isLoading) {
            focusManager.clearFocus()
            onIntent(SignInIntent.SignInClicked)
        }
    }

    AuthLayout(
        title = stringResource(R.string.signInTitle),
        onSkipClick = {
            if (!state.isLoading) {
                onIntent(SignInIntent.SkipClicked)
            }
        },
        footer = {
            SignUpFooter(
                enabled = !state.isLoading,
                onSignUpClick = {
                    onIntent(SignInIntent.SignUpClicked)
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
                    onIntent(SignInIntent.EmailChanged(it))
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
                    onIntent(SignInIntent.PasswordChanged(it))
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
                    onIntent(SignInIntent.ForgotPasswordClicked)
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

            state.signInError?.let { errorId ->
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
                        R.string.Loadingg
                    } else {
                        R.string.sinInButton
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
                    onIntent(SignInIntent.FacebookClicked)
                },
                onGoogleClick = {
                    onIntent(SignInIntent.GoogleClicked)
                },
                onAppleClick = {
                    onIntent(SignInIntent.AppleClicked)
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
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.Accprompt),
            style = TextStyles.AuthBody,
            color = Text2,
            modifier = Modifier.align(Alignment.CenterVertically),
        )

        TextButton(
            onClick = onSignUpClick,
            enabled = enabled,
        ) {
            Text(
                text = stringResource(R.string.signin_signup_link),
                style = TextStyles.AuthBody,
                color = MovieWhite,
            )
        }
    }
}