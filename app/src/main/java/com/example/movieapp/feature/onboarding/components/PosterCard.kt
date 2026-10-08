package com.example.movieapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieapp.ui.theme.MoviePlaceholder

@Composable
fun PosterMovement(
    urls: List<String?>,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val posters = if (isLoading) {
        List(8) { null }
    } else {
        urls
            .filterNotNull()
            .filter { it.isNotBlank() }
            .take(8)
    }

    if (posters.isEmpty()) return

    val shimmer = if (isLoading) rememberShimmer() else null

    val top = posters.take(4)

    val bottom = posters
        .drop(4)
        .ifEmpty { top.reversed() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        PosterCarousel(
            urls = top,
            reverse = true,
            isLoading = isLoading,
            shimmer = shimmer
        )

        PosterCarousel(
            urls = bottom,
            reverse = false,
            isLoading = isLoading,
            shimmer = shimmer
        )
    }
}

@Composable
private fun PosterCarousel(
    urls: List<String?>,
    reverse: Boolean,
    isLoading: Boolean,
    shimmer: State<Float>?,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(urls, isLoading) {

        if (isLoading || urls.isEmpty()) {
            return@LaunchedEffect
        }

        while (true) {
            listState.animateScrollBy(
                value = 143f,
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = LinearEasing
                )
            )
        }
    }

    LazyRow(
        state = listState,
        reverseLayout = reverse,
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(182.dp)
    ) {

        if (isLoading) {

            items(4) {
                PosterCard(
                    url = null,
                    shimmer = shimmer
                )
            }

        } else {

            items(
                count = Int.MAX_VALUE
            ) { index ->

                PosterCard(
                    url = urls[index % urls.size],
                    shimmer = null
                )
            }
        }
    }
}

@Composable
private fun PosterCard(
    url: String?,
    shimmer: State<Float>?,
) {
    Box(
        modifier = Modifier
            .size(
                width = 133.dp,
                height = 182.dp
            )
            .clip(RoundedCornerShape(8.dp))
            .background(MoviePlaceholder)
            .drawBehind {

                if (shimmer != null) {

                    val start =
                        size.width * (shimmer.value * 2f - 1f)

                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.5f),
                                Color.Transparent
                            ),
                            start = Offset(start, 0f),
                            end = Offset(
                                start + size.width,
                                0f
                            )
                        )
                    )
                }
            }
    ) {

        if (
            shimmer == null &&
            !url.isNullOrBlank()
        ) {
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}
