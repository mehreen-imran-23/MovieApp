package com.example.movieapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.MovieSurface
import com.example.movieapp.ui.theme.TextStyles
import com.example.movieapp.ui.theme.MovieWhite

@Composable
fun GenreBox(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = if (selected) RedPrime else MovieSurface,
        contentColor = MovieWhite,
    ) {
        Box(
            modifier = Modifier
                .heightIn(min = 32.dp)
                .padding(
                    horizontal = 14.dp,
                    vertical = 7.dp,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = TextStyles.GenreLabel,
                textAlign = TextAlign.Center,
            )
        }
    }
}