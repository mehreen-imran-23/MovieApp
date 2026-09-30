package com.example.movieapp.feature.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movieapp.feature.auth.AuthActivity
import com.example.movieapp.navigation.OnboardingNavHost
import com.example.movieapp.ui.theme.MovieAppTheme

class OnboardingActivity : ComponentActivity() {

    private var opening = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
        )


        setContent {
            MovieAppTheme {
                OnboardingNavHost(
                    versionName = "1.0.1",
                    onOpenSignIn = ::openSignIn,
                )
            }
        }
    }

    private fun openSignIn() {
        startActivity(
            Intent(this, AuthActivity::class.java)
        )
    }
}