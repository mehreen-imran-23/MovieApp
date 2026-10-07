package com.example.movieapp.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.R
import com.example.movieapp.data.cleanData.HomeMovie
import com.example.movieapp.ui.common_components.MovieBackButton
import com.example.movieapp.ui.components.MoviePoster
import com.example.movieapp.ui.theme.MovieBg
import com.example.movieapp.ui.theme.MovieSurface
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.SearchPlaceholder
import com.example.movieapp.ui.theme.TextMuted
import com.example.movieapp.ui.theme.TextStyles

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SearchContent(
        state = state,
        onIntent = { intent ->
            viewModel.onIntent(intent)
        },
        modifier = modifier,
    )
}

@Composable
fun SearchContent(
    state: SearchUiState,
    onIntent: (SearchIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val gridState = rememberLazyGridState()

    val showRecent = state.query.isBlank()

    val movies = if (showRecent) {
        state.recentMovies
    } else {
        state.movies
    }

    LaunchedEffect(
        gridState, state.NextPage, state.isLoadingMore,
    ) {
        snapshotFlow {
            val layout = gridState.layoutInfo
            val lastVisible = layout.visibleItemsInfo.lastOrNull()?.index

            lastVisible != null &&
                    lastVisible >= layout.totalItemsCount - 1
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MovieBg,
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding(),
        ) {
            SearchHeader(
                query = state.query,
                onQueryChanged = {
                    onIntent(SearchIntent.QueryChanged(it))
                },
                onBackClick = {
                    onIntent(SearchIntent.BackClicked)
                },
                onSearch = {
                    focusManager.clearFocus()
                    keyboard?.hide()
                    onIntent(SearchIntent.SearchClicked)
                },
            )

            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 24.dp,
                    bottom = 24.dp,
                ),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                item(
                    key = "heading",
                    span = { GridItemSpan(maxLineSpan) },
                ) {
                    Text(
                        text = stringResource(
                            if (showRecent) {
                                R.string.search_recent
                            } else {
                                R.string.search_res
                            }
                        ),
                        style = TextStyles.Home,
                        color = MovieWhite,
                    )
                }

                state.errorMsg?.let { errorId ->
                    item(
                        key = "search_error",
                        span = { GridItemSpan(maxLineSpan) },
                    ) {
                        Text(
                            text = stringResource(errorId),
                            style = TextStyles.AuthBody,
                            color = RedPrime,
                        )
                    }
                }

                if (state.isLoading) {
                    item(
                        key = "initial_loading",
                        span = { GridItemSpan(maxLineSpan) },
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                color = RedPrime,
                            )
                        }
                    }
                } else {

                    items(
                        items = movies,
                        key = { movie -> movie.id },
                    ) { movie ->
                        SearchMovieCard(
                            movie = movie,
                            onClick = {
                                onIntent(
                                    SearchIntent.MovieClicked(movie.id)
                                )
                            },
                        )
                    }

                    if (
                        !showRecent &&
                        state.hasSearched &&
                        movies.isEmpty() &&
                        !state.NextPage &&
                        state.errorMsg == null
                    ) {
                        item(
                            key = "empty_results",
                            span = { GridItemSpan(maxLineSpan) },
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.search_no_results
                                ),
                                style = TextStyles.AuthBody,
                                color = TextMuted,
                            )
                        }
                    }

                    if (!showRecent && state.isLoadingMore) {
                        item(
                            key = "loading_more",
                            span = { GridItemSpan(maxLineSpan) },
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(
                                    color = RedPrime,
                                )
                            }
                        }
                    }

                    if (!showRecent) {
                        state.loadMoreError?.let { errorId ->

                            item(
                                key = "load_more_error",
                                span = { GridItemSpan(maxLineSpan) },
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        text = stringResource(errorId),
                                        style = TextStyles.AuthBody,
                                        color = RedPrime,
                                    )

                                    TextButton(
                                        onClick = {
                                            onIntent(SearchIntent.LoadMore)
                                        },
                                    ) {
                                        Text(
                                            text = stringResource(
                                                R.string.search_retry
                                            ),
                                            color = MovieWhite,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchHeader(
    query: String,
    onQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onSearch: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        MovieBackButton(
            onClick = onBackClick,
        )

        BasicTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = TextStyles.SearchText.copy(
                color = MovieWhite,
            ),
            cursorBrush = SolidColor(RedPrime),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() },
            ),
            decorationBox = { innerTextField ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .background(
                            MovieSurface,
                            RoundedCornerShape(50),
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(
                                    R.string.search_movies
                                ),
                                style = TextStyles.SearchText,
                                color = SearchPlaceholder,
                            )
                        }

                        innerTextField()
                    }

                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = null,
                        tint = MovieWhite,
                        modifier = Modifier.size(22.dp),
                    )
                }
            },
        )
    }
}

@Composable
private fun SearchMovieCard(
    movie: HomeMovie,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MoviePoster(
            posterUrl = movie.posterUrl,
            cornerRadius = 24.dp,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MovieSurface),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    contentDescription = null,
                    tint = MovieWhite,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        Text(
            text = movie.title,
            style = TextStyles.SmallLabel,
            color = MovieWhite,
        )
    }
}