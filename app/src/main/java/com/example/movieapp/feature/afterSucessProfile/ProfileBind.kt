package com.example.movieapp.feature.afterSucessProfile

import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.movieapp.R
import com.example.movieapp.databinding.SignupSuccessBinding
import com.example.movieapp.ui.components.AuthTextField
import com.example.movieapp.ui.components.MovieButton
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.RedPrime
import kotlinx.coroutines.launch

fun profileBind(
    binding: SignupSuccessBinding,
    viewModel: ProfileViewModel,
    lifecycleOwner: LifecycleOwner,
) {
    with(binding)
    {
        val strategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed

        nameField.setViewCompositionStrategy(strategy)
        phoneField.setViewCompositionStrategy(strategy)
        cityField.setViewCompositionStrategy(strategy)
        continueButton.setViewCompositionStrategy(strategy)

        nameField.setContent {
            MovieAppTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                val focusManager = LocalFocusManager.current

                AuthTextField(
                    value = state.name.input,
                    onValueChange = {
                        viewModel.onIntent(
                            ProfileIntent.NameChanged(it)
                        )
                    },
                    placeholder = stringResource(R.string.name),
                    enabled = !state.isLoading,
                    errorMsg = state.name.errorMsg?.let { stringResource(it) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    ),
                )
            }
        }

        phoneField.setContent {
            MovieAppTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                val focusManager = LocalFocusManager.current

                AuthTextField(
                    value = state.phoneNumber.input,
                    onValueChange = {
                        viewModel.onIntent(
                            ProfileIntent.PhoneNumber(it)
                        )
                    },
                    placeholder = stringResource(R.string.phone_number),
                    enabled = !state.isLoading,
                    errorMsg = state.phoneNumber.errorMsg?.let {
                        stringResource(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                    ),
                )
            }
        }

        cityField.setContent {
            MovieAppTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                val focusManager = LocalFocusManager.current

                AuthTextField(
                    value = state.city.input,
                    onValueChange = {
                        viewModel.onIntent(
                            ProfileIntent.City(it)
                        )
                    },
                    placeholder = stringResource(R.string.city_pincode),
                    enabled = !state.isLoading,
                    errorMsg = state.city.errorMsg?.let { stringResource(it) },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        },
                    ),
                )
            }
        }

        continueButton.setContent {
            MovieAppTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                MovieButton(
                    text = stringResource(
                        if (state.isLoading) {
                            R.string.profile_saving
                        } else {
                            R.string.continue_btn
                        }
                    ),
                    onClick = {
                        root.clearFocus()
                        ViewCompat.getWindowInsetsController(root)
                            ?.hide(WindowInsetsCompat.Type.ime())

                        viewModel.onIntent(ProfileIntent.ContinueClicked)
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    bg = RedPrime,
                    content = MovieWhite,
                )
            }
        }

        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val error = state.errorMsg
                    if (error != null) {
                        profileError.text = root.context.getString(error)
                        profileError.visibility = View.VISIBLE
                    } else {
                        profileError.visibility = View.GONE
                    }
                }
            }
        }
    }
}