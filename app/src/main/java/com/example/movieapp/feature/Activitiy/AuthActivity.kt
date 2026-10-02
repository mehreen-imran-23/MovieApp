package com.example.movieapp.feature.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movieapp.feature.Activitiy.HomeActivity
import com.example.movieapp.feature.afterSucessProfile.ProfileActivity
import com.example.movieapp.navigation.AuthNavHost
import com.example.movieapp.ui.theme.MovieAppTheme

class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        setContent {
            MovieAppTheme {
                AuthNavHost(
                    onOpenHome = {
                        val intent = Intent(
                            this@AuthActivity,
                            HomeActivity::class.java,
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }

                        startActivity(intent)
                    },
                    onOpenProfile = {
                        startActivity(
                            Intent(
                                this@AuthActivity,
                                ProfileActivity::class.java,
                            )
                        )

                        finish()
                    },
                )
            }
        }
    }
}