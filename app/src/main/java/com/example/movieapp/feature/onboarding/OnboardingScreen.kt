package com.example.movieapp.feature.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.R
import com.example.movieapp.ui.components.GenreBox
import com.example.movieapp.ui.components.GenrePlaceholder
import com.example.movieapp.ui.components.MovieButton
import com.example.movieapp.ui.components.PageIndicator
import com.example.movieapp.ui.components.PosterMovement
import com.example.movieapp.ui.components.SkipButton
import com.example.movieapp.ui.components.rememberShimmer
import com.example.movieapp.ui.theme.MovieBg
import com.example.movieapp.ui.theme.Text2
import com.example.movieapp.ui.theme.TextStyles
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private val screenPadding = 26.dp
private val contentWidth = 340.dp
private val genreWidth = 342.dp
private const val placeholderCount = 8
private const val pagescroll = 300

private enum class OnboardingStep {
    Poster,
    Genre
}

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingContent(
        state = state,
        onIntent = { intent -> viewModel.onIntent(intent) },
        modifier = modifier,
    )
}

@Composable
private fun OnboardingContent(
    state: OnboardingState,
    onIntent: (OnboardingIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pageState = rememberPagerState(
        pageCount = { OnboardingStep.entries.size }
    )
    val scope = rememberCoroutineScope()

    BackHandler(enabled = pageState.currentPage > 0) {
        scope.launch {
            pageState.animateScrollToPage(
                page = 0,
                animationSpec = tween(300),
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MovieBg,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                SkipButton(
                    onClick = {
                        scope.launch {
                            pageState.scrollToPage(1)
                            onIntent(OnboardingIntent.Skip)
                        }
                    }
                )
            }
        },
        bottomBar = {
            OnboardingBottomBar(
                currPage = pageState.currentPage,
                enabled = !pageState.isScrollInProgress,
                onNext = {
                    if (
                        pageState.currentPage ==
                        OnboardingStep.Poster.ordinal
                    ) {
                        scope.launch {
                            pageState.animateScrollToPage(
                                page = OnboardingStep.Genre.ordinal,
                                animationSpec = tween(pagescroll),
                            )
                        }
                    } else {
                        onIntent(OnboardingIntent.Next)
                    }
                },
            )
        },
    ) { innerPadding ->
        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) { page ->
            when (OnboardingStep.entries[page]) {

                OnboardingStep.Poster -> OnboardingPage(
                    description = stringResource(R.string.onboarding1),
                    errorMsg = state.errorMsg,
                ) {
                    PosterMovement(
                        urls = state.posterUrls,
                        isLoading = state.isLoading,
                    )
                }

                OnboardingStep.Genre -> OnboardingPage(
                    description = stringResource(R.string.genres_select),
                    errorMsg = state.errorMsg,
                ) {
                    GenreList(
                        state = state,
                        onGenreClick = { genreId ->
                            onIntent(
                                OnboardingIntent.GenreClicked(genreId)
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingBottomBar(
    currPage: Int,
    enabled: Boolean,
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                start = 26.dp,
                end = 26.dp,
                top = 20.dp,
                bottom = 51.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        MovieButton(
            text = stringResource(R.string.next),
            onClick = onNext,
            enabled = enabled,
            modifier = Modifier
                .widthIn(max = contentWidth)
                .fillMaxWidth()
                .heightIn(min = 53.dp),
        )

        PageIndicator(
            currentPage = currPage
        )
    }
}

@Composable
private fun OnboardingPage(
    description: String,
    errorMsg: String?,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            if (errorMsg != null) {
                Text(
                    text = errorMsg,
                    color = Text2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(screenPadding),
                )
            } else {
                content()
            }
        }

        Text(
            text = description,
            style = TextStyles.Onboarding,
            lineHeight = 30.sp,
            color = Text2,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .widthIn(max = contentWidth)
                .fillMaxWidth()
                .heightIn(min = 60.dp),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenreList(
    state: OnboardingState,
    onGenreClick: (Int) -> Unit,
) {
    val shimmer =
        if (state.isLoading) {
            rememberShimmer()
        } else {
            null
        }

    FlowRow(
        modifier = Modifier
            .widthIn(max = genreWidth)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            6.dp,
            Alignment.CenterHorizontally
        ),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (state.isLoading && shimmer != null) {
            repeat(placeholderCount) {
                GenrePlaceholder(
                    shimmer = shimmer
                )
            }
        } else {
            state.genres.forEach { genre ->
                key(genre.id) {
                    GenreBox(
                        text = genre.name,
                        selected = genre.id in state.selectedGenre,
                        onClick = {
                            onGenreClick(genre.id)
                        },
                    )
                }
            }
        }
    }
}