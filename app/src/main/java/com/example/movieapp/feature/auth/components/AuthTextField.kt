package com.example.movieapp.ui.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.movieapp.ui.theme.InputBorder
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.Text2
import com.example.movieapp.ui.theme.TextStyles


@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMsg: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,

        placeholder = {
            Text(
                text = placeholder,
                style = TextStyles.AuthBody
            )
        },

        textStyle = TextStyles.AuthBody.copy(
            color = MovieWhite
        ),

        shape = RoundedCornerShape(16.dp),

        isError = errorMsg != null,

        supportingText = if (errorMsg != null) {
            {
                Text(
                    text = errorMsg,
                    style = TextStyles.Terms,
                )
            }
        } else {
            null
        },

        trailingIcon = trailingIcon,

        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MovieWhite,
            unfocusedBorderColor = InputBorder,
            errorBorderColor = RedPrime,
            focusedTextColor = MovieWhite,
            unfocusedTextColor = MovieWhite,
            focusedPlaceholderColor = Text2,
            unfocusedPlaceholderColor = Text2,
            cursorColor = RedPrime
        )
    )
}


