package com.example.movieapp.feature.Activitiy

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.movieapp.R
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.theme.MovieBg
import com.example.movieapp.ui.theme.MovieWhite
import com.example.movieapp.ui.theme.TextStyles

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        setContent {
            MovieAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MovieBg)
                        .safeDrawingPadding(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.home_placeholder),
                        style = TextStyles.AuthBody,
                        color = MovieWhite,
                    )
                }
            }
        }
    }
}