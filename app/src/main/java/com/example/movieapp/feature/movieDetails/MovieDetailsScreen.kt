package com.example.movieapp.feature.moviedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.R
import com.example.movieapp.data.model.MovieDetailsData
import com.example.movieapp.feature.movieDetails.MovieDetailsIntent
import com.example.movieapp.ui.common_components.MovieBackButton
import com.example.movieapp.ui.components.MovieButton
import com.example.movieapp.ui.components.MoviePoster
import com.example.movieapp.ui.theme.BookGradientS
import com.example.movieapp.ui.theme.MovieBg
import com.example.movieapp.ui.theme.MovieSurface
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.TextMuted
import com.example.movieapp.ui.theme.TextStyles

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    MovieDetailsContent(
        state = state, onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun MovieDetailsContent(
    state: MovieDetailsState,
    onIntent: (MovieDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MovieBg,
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal
        ),
        bottomBar = {
            if (state.movie != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp,
                        )
                        .clip(RoundedCornerShape(24.dp))
                        .background(MovieSurface)
                        .padding(10.dp),
                ) {
                    MovieButton(
                        text = stringResource(R.string.book_tickets),
                        onClick = {},
                        bg = RedPrime,
                        content = MovieWhite,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            val movie = state.movie

            if (movie != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    MoviePoster(
                        posterUrl = movie.posterUrl,
                        cornerRadius = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(390.dp),
                    )

                    MovieDetailsPanel(
                        movie = movie,
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 285.dp,
                                bottom = 16.dp,
                            ),
                    )
                }
            } else if (state.isLoading) {
                CircularProgressIndicator(
                    color = RedPrime,
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                state.errorMsg?.let { errorId ->
                    Text(
                        text = stringResource(errorId),
                        style = TextStyles.AuthBody,
                        color = MovieWhite,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MovieBackButton(
                    onClick = {
                        onIntent(MovieDetailsIntent.BackClicked)
                    },
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MovieSurface, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null,
                        tint = MovieWhite,
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieDetailsPanel(
    movie: MovieDetailsData,
    modifier: Modifier = Modifier,
) {
    val unavailable = stringResource(R.string.details_unavailable)

    val duration = if (movie.durationMinutes != null) {
        stringResource(
            R.string.movie_duration,
            movie.durationMinutes / 60,
            movie.durationMinutes % 60,
        )
    } else {
        unavailable
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(40.dp))
            .background(MovieSurface)
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = movie.title,
            style = TextStyles.Home,
            color = MovieWhite,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = if (movie.genres.isEmpty()) {
                    unavailable
                } else {
                    movie.genres.joinToString(", ")
                },
                style = TextStyles.Metadata,
                color = MovieWhite,
                modifier = Modifier.weight(1f),
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(BookGradientS)
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.watch_trailer),
                    style = TextStyles.SmallLabel,
                    color = MovieWhite,
                )

                Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    contentDescription = null,
                    tint = MovieWhite,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        HorizontalDivider(color = TextMuted)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MovieDetailValue(
                label = stringResource(R.string.rating),
                value = movie.certification ?: unavailable,
                modifier = Modifier.weight(1f),
            )

            MovieDetailValue(
                label = stringResource(R.string.duration),
                value = duration,
                modifier = Modifier.weight(1f),
            )

            MovieDetailValue(
                label = stringResource(R.string.release_date),
                value = movie.releaseDate ?: unavailable,
                modifier = Modifier.weight(1f),
            )
        }

        MovieDetailValue(
            label = stringResource(R.string.languages),
            value = if (movie.languages.isEmpty()) {
                unavailable
            } else {
                movie.languages.joinToString(", ")
            },
        )

        HorizontalDivider(color = TextMuted)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.plot),
                style = TextStyles.Home,
                color = MovieWhite,
            )

            Text(
                text = if (movie.overview.isBlank()) {
                    unavailable
                } else {
                    movie.overview
                },
                style = TextStyles.Metadata,
                color = MovieWhite,
            )
        }

        HorizontalDivider(color = TextMuted)

        Text(
            text = stringResource(R.string.cast),
            style = TextStyles.Home,
            color = MovieWhite,
        )

        if (movie.cast.isEmpty()) {
            Text(
                text = unavailable,
                style = TextStyles.Metadata,
                color = MovieWhite,
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(movie.cast) { actor ->
                    MoviePoster(
                        posterUrl = actor.imageUrl,
                        contentDescription = actor.name,
                        cornerRadius = 16.dp,
                        modifier = Modifier.size(
                            width = 64.dp,
                            height = 72.dp,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieDetailValue(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            style = TextStyles.Metadata,
            color = MovieWhite,
        )

        Text(
            text = value,
            style = TextStyles.Metadata,
            color = MovieWhite,
        )
    }
}