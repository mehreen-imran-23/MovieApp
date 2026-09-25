package com.example.movieapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movieapp.ui.theme.RedPrime
import com.example.movieapp.ui.theme.MovieSurface
import com.example.movieapp.ui.theme.RedPrime

@Composable
fun OnboardingPageIndicator(
    currentPage: Int,
    modifier: Modifier = Modifier,
    pageCount: Int = 2,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(
                        width = 52.dp,
                        height = 4.dp,
                    )
                    .background(
                        color = if (index == currentPage) {
                            RedPrime
                        } else {
                            MovieSurface
                        },
                        shape = RoundedCornerShape(2.dp),
                    ),
            )
        }
    }
}