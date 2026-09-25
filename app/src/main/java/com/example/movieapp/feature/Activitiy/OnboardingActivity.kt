package com.example.movieapp.feature.onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movieapp.navigation.OnboardingNavHost
import com.example.movieapp.ui.theme.MovieAppTheme

class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MovieAppTheme {
                OnboardingNavHost(
                    versionName = "1.0.1",
                )
            }
        }
    }
}