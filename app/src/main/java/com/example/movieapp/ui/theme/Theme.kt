package com.example.movieapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MovieColorScheme = darkColorScheme(
    primary = RedPrime,
    onPrimary = MovieWhite,

    background = MovieBg,
    onBackground = MovieWhite,

    surface = MovieSurface,
    onSurface = MovieWhite,

    onSurfaceVariant = Text2,

    outline = InputBorder,
    outlineVariant = Divider,
)

@Composable
fun MovieAppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = MovieColorScheme,
        typography = Typography,
        content = content,
    )
}