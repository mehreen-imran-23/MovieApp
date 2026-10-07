package com.example.movieapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun MovieAppTheme(
    content: @Composable () -> Unit,
) {
    val movieColorScheme = darkColorScheme(
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

    MaterialTheme(
        colorScheme = movieColorScheme,
        typography = Typography,
        content = content,
    )
}