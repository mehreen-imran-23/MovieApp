package com.example.movieapp.feature.onboarding

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movieapp.feature.Activitiy.HomeActivity
import com.example.movieapp.feature.auth.AuthActivity
import com.example.movieapp.navigation.OnboardingNavHost
import com.example.movieapp.ui.theme.MovieAppTheme

class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        setContent {
            MovieAppTheme {
                OnboardingNavHost(
                    versionName = "1.0.1",

                    onOpenSignIn = { closeOnboarding ->
                        startActivity(
                            Intent(
                                this@OnboardingActivity,
                                AuthActivity::class.java,
                            )
                        )

                        if (closeOnboarding) {
                            finish()
                        }
                    },
                    onOpenHome = {
                        startActivity(
                            Intent(
                                this@OnboardingActivity,
                                HomeActivity::class.java,
                            )
                        )
                        finish()
                    },
                )
            }
        }
    }
}