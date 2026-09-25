package com.example.movieapp.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movieapp.R
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.TextStyles
import com.example.movieapp.ui.theme.MovieWhite

@Composable
fun SplashScreen(
    versionName: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(RedPrime),
    ) {
        Image(
            painter = painterResource(R.drawable.movie_logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .size(
                    width = 86.5.dp,
                    height = 74.96667.dp,
                ),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(24.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Text(
                text = stringResource(
                    R.string.version,
                    versionName,
                ),
                style = TextStyles.SmallLabel,
                color = MovieWhite,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(
    name = "Small phone",
    widthDp = 360,
    heightDp = 640,
)
@Preview(
    name = "Large phone",
    widthDp = 412,
    heightDp = 915,
)
@Composable
private fun SplashScreenPreview() {
    MovieAppTheme {
        SplashScreen(versionName = "1.0.1")
    }
}