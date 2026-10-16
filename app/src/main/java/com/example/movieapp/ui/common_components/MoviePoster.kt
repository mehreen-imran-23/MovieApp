package com.example.movieapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieapp.ui.theme.MovieSurface

@Composable
fun MoviePoster(
    posterUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    cornerRadius: Dp = 16.dp,
    overlay: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(MovieSurface),
    ) {
        AsyncImage(
            model = posterUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )

        overlay()
    }
}