package com.example.movieapp.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.R
import com.example.movieapp.data.model.HomeMovie
import com.example.movieapp.ui.components.MoviePoster
import com.example.movieapp.ui.theme.BookGradientE
import com.example.movieapp.ui.theme.BookGradientM
import com.example.movieapp.ui.theme.BookGradientS
import com.example.movieapp.ui.theme.MovieBg
import com.example.movieapp.ui.theme.MovieSurface
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.NavGradientE
import com.example.movieapp.ui.theme.NavGradientS
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.TextStyles


private enum class BottomTab {
    Movies,
    Tv,
    Tickets,
    More,
}


@Composable
fun HomeScreen(
    viewModel: HomeViewModel, modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        onIntent = { intent ->
            viewModel.onIntent(intent)
        },
        modifier = modifier,
    )
}

@Composable
fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MovieBg,
        bottomBar = {
            HomeBottomBar(
                onLogout = {
                    onIntent(HomeIntent.LogoutClicked)
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 40.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {

                item {
                    HomeHeader(
                        userName = state.userName,
                        city = state.city,
                        onSearchClick = {
                            onIntent(HomeIntent.SearchClicked)
                        },
                        onProfileClick = {
                            //only clickable
                        },
                    )
                }

                item {
                    state.trendingMovies.firstOrNull()?.let { movie ->
                        TrendingCard(
                            movie = movie,
                            onBookClick = {
                                onIntent(
                                    HomeIntent.BookClicked(movie.id)
                                )
                            },
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                            ),
                        )
                    }
                }

                item {
                    RecommendedSection(
                        movies = state.recommendedMovies,
                        onMovieClick = { movieId ->
                            onIntent(
                                HomeIntent.BookClicked(movieId)
                            )
                        },
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = 24.dp,
                        bottom = 12.dp,
                    )
                    .size(52.dp)
                    .background(
                        color = RedPrime,
                        shape = CircleShape,
                    )
                    .clickable {
                        // Filter clickable
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.FilterAlt,
                    contentDescription = stringResource(
                        R.string.filter
                    ),
                    tint = MovieWhite,
                    modifier = Modifier.size(30.dp),
                )
            }
        }
    }
}


@Composable
private fun HomeHeader(
    userName: String,
    city: String,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {

            Text(
                text = if (userName.isBlank()) {
                    stringResource(R.string.guest_greeting)
                } else {
                    stringResource(
                        R.string.home_greeting,
                        userName,
                    )
                },
                style = TextStyles.Home,
                color = MovieWhite,
            )

            if (city.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(
                        text = city,
                        style = TextStyles.Metadata,
                        color = RedPrime,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(
                            weight = 1f,
                            fill = false,
                        ),
                    )

                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = RedPrime,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }

        IconButton(
            onClick = onSearchClick,
            modifier = Modifier
                .size(50.dp)
                .background(
                    MovieSurface,
                    CircleShape,
                ),
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(
                    R.string.search
                ),
                tint = MovieWhite,
                modifier = Modifier.size(28.dp),
            )
        }

        IconButton(
            onClick = onProfileClick,
            modifier = Modifier
                .size(50.dp)
                .background(
                    MovieSurface,
                    CircleShape,
                ),
        ) {
            Icon(
                imageVector = Icons.Outlined.PersonOutline,
                contentDescription = stringResource(
                    R.string.open_profile
                ),
                tint = MovieWhite,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}


@Composable
private fun TrendingCard(
    movie: HomeMovie,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {

        MoviePoster(
            posterUrl = movie.posterUrl,
            contentDescription = movie.title,
            cornerRadius = 36.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 180.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .background(
                        MovieSurface,
                        RoundedCornerShape(50),
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {

                Text(
                    text = stringResource(
                        R.string.watch_trailer
                    ),
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MovieSurface,
                        RoundedCornerShape(40.dp),
                    )
                    .padding(
                        horizontal = 24.dp,
                        vertical = 16.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {

                    Text(
                        text = stringResource(
                            R.string.trending
                        ),
                        style = TextStyles.SmallLabel,
                        color = MovieWhite,
                    )

                    Text(
                        text = movie.title,
                        style = TextStyles.Home,
                        color = MovieWhite,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {

                        movie.certification
                            ?.takeIf { it.isNotBlank() }
                            ?.let { certification ->

                                Text(
                                    text = certification,
                                    style = TextStyles.Certification,
                                    color = RedPrime,
                                )
                            }

                        Text(
                            text = movie.language,
                            style = TextStyles.Metadata,
                            color = MovieWhite,
                        )
                    }

                    if (movie.genres.isNotEmpty()) {
                        Text(
                            text = movie.genres.joinToString(" · "),
                            style = TextStyles.Metadata,
                            color = MovieWhite,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {

                    Surface(
                        onClick = onBookClick,
                        shape = RoundedCornerShape(50),
                        color = Color.Transparent,
                        contentColor = MovieWhite,
                    ) {

                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            BookGradientS,
                                            BookGradientM,
                                            BookGradientE,
                                        )
                                    )
                                )
                                .heightIn(min = 48.dp)
                                .padding(
                                    horizontal = 18.dp,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {

                            Text(
                                text = stringResource(
                                    R.string.book
                                ),
                                style = TextStyles.MovieLabel,
                            )
                        }
                    }

                    Text(
                        text = stringResource(
                            R.string.formats
                        ),
                        style = TextStyles.SmallLabel,
                        color = MovieWhite,
                    )
                }
            }
        }
    }
}


@Composable
private fun RecommendedSection(
    movies: List<HomeMovie>,
    onMovieClick: (Int) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            Text(
                text = stringResource(
                    R.string.recommended_movies
                ),
                style = TextStyles.Home,
                color = MovieWhite,
                modifier = Modifier.weight(1f),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    text = stringResource(
                        R.string.see_all
                    ),
                    style = TextStyles.Metadata,
                    color = RedPrime,
                )

                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = RedPrime,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(
                horizontal = 14.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {

            items(
                items = movies,
                key = { movie -> movie.id },
            ) { movie ->

                RecommendedCard(
                    movie = movie,
                    onClick = {
                        onMovieClick(movie.id)
                    },
                )
            }
        }
    }
}


@Composable
private fun RecommendedCard(
    movie: HomeMovie,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(166.dp)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        MoviePoster(
            posterUrl = movie.posterUrl,
            cornerRadius = 32.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(222.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .size(42.dp)
                    .background(
                        color = MovieSurface,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    contentDescription = null,
                    tint = MovieWhite,
                    modifier = Modifier.size(22.dp),
                )
            }
        }

        Text(
            text = movie.title,
            style = TextStyles.MovieLabel,
            color = MovieWhite,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun HomeBottomBar(
    onLogout: () -> Unit,
) {
    var selectedTab by rememberSaveable {
        mutableStateOf(BottomTab.Movies)
    }

    var showMenu by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                horizontal = 20.dp,
                vertical = 8.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(MovieSurface)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for (tab in BottomTab.entries) {
                val selected = selectedTab == tab
                val icon = when (tab) {
                    BottomTab.Movies ->
                        painterResource(R.drawable.movie_icon)

                    BottomTab.Tv ->
                        painterResource(R.drawable.tv_icon)

                    BottomTab.Tickets ->
                        painterResource(R.drawable.ticket_icon)

                    BottomTab.More ->
                        rememberVectorPainter(Icons.Outlined.MoreHoriz)
                }

                val label = stringResource(
                    when (tab) {
                        BottomTab.Movies -> R.string.nav_movies
                        BottomTab.Tv -> R.string.tv
                        BottomTab.Tickets -> R.string.tickets
                        BottomTab.More -> R.string.more
                    }
                )

                Box {
                    BottomButton(
                        icon = icon,
                        label = label,
                        selected = selected,
                        onClick = {
                            if (tab == BottomTab.More) {
                                showMenu = true
                            } else {
                                selectedTab = tab
                            }
                        },
                        modifier = if (selected) {
                            Modifier
                                .width(112.dp)
                                .height(58.dp)
                        } else {
                            Modifier.size(58.dp)
                        },
                    )

                    if (tab == BottomTab.More) {
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = {
                                showMenu = false
                            },
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.logout),
                                        color = MovieWhite,
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onLogout()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomButton(
    icon: Painter,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val background = if (selected) {
        Modifier.background(RedPrime)
    } else {
        Modifier.background(
            Brush.horizontalGradient(
                0.0592f to NavGradientS,
                0.9228f to NavGradientE,
            )
        )
    }

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = Color.Transparent,
        contentColor = MovieWhite,
        selected = selected,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .then(background)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 6.dp,
                alignment = Alignment.CenterHorizontally,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = icon,
                contentDescription = if (selected) {
                    null
                } else {
                    label
                },
                tint = MovieWhite,
                modifier = Modifier.size(26.dp),
            )

            if (selected) {
                Text(
                    text = label,
                    style = TextStyles.NavLabel,
                    color = MovieWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}