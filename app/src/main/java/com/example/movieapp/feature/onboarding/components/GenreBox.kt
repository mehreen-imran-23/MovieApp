package com.example.movieapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.movieapp.ui.theme.MovieSurface
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.TextStyles

private val shape = RoundedCornerShape(4.dp)
private val minHeight = 32.dp
private val placeholderWidth = 72.dp

@Composable
fun GenreBox(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                if (selected) RedPrime else MovieSurface
            )
            .clickable(onClick = onClick)
            .heightIn(min = minHeight)
            .padding(
                horizontal = 14.dp,
                vertical = 7.dp
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = TextStyles.GenreLabel,
            color = MovieWhite,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun GenrePlaceholder(
    shimmer: State<Float>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(
                width = placeholderWidth,
                height = minHeight,
            )
            .clip(shape)
            .background(MovieSurface)
            .drawBehind {
                val start =
                    size.width * (shimmer.value * 2f - 1f)

                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.25f),
                            Color.Transparent,
                        ),
                        start = Offset(
                            start,
                            0f
                        ),
                        end = Offset(
                            start + size.width,
                            0f
                        ),
                    )
                )
            }
    )
}