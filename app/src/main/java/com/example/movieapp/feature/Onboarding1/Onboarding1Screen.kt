package com.example.movieapp.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.movieapp.R
import com.example.movieapp.feature.Onboarding1.Onboarding1Intent
import com.example.movieapp.ui.components.MovieButton
import com.example.movieapp.ui.components.OnboardingPageIndicator
import com.example.movieapp.ui.theme.MovieBg
import com.example.movieapp.ui.theme.MoviePlaceholder
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.Text2
import com.example.movieapp.ui.theme.TextStyles

@Composable
fun OnboardingPostersScreen(
    viewModel: Onboarding1ViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingPostersContent(
        posterUrls = state.posterUrls,
        onNext = {
            viewModel.onIntent(Onboarding1Intent.Next)
        },
        onSkip = {
            viewModel.onIntent(Onboarding1Intent.Skip)
        },
        modifier = modifier,
    )
}

@Composable
private fun OnboardingPostersContent(
    posterUrls: List<String>,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MovieBg)
            .safeDrawingPadding(),
    ) {
        val availableHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = availableHeight),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = onSkip,
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 12.dp,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.Skip),
                        style = TextStyles.SkipLabel,
                        color = Text2,
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            PosterRows(posterUrls = posterUrls)

            // Expands on tall screens; remains scrollable on short screens.
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp),
            )

            Text(
                text = stringResource(R.string.onboarding1),
                style = TextStyles.Onboarding,
                color = Text2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 26.dp)
                    .widthIn(max = 220.dp),
            )

            Spacer(Modifier.height(48.dp))

            MovieButton(
                text = stringResource(R.string.Next),
                onClick = onNext,
                modifier = Modifier
                    .padding(horizontal = 26.dp)
                    .widthIn(max = 420.dp)
                    .fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            OnboardingPageIndicator(currentPage = 0)

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PosterRows(
    posterUrls: List<String>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        repeat(2) { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        rememberScrollState(
                            initial = if (row == 0) 0 else 40
                        )
                    ),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                repeat(4) { column ->
                    val imageUrl = posterUrls.getOrNull(row * 4 + column)

                    Box(
                        modifier = Modifier
                            .size(width = 133.dp, height = 182.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MovieWhite),
                    ) {
                        if (imageUrl != null) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }
            }
        }
    }
}