package com.example.movieapp.feature.Activitiy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movieapp.feature.auth.AuthActivity
import com.example.movieapp.navigation.HomeNavHost
import com.example.movieapp.ui.theme.MovieAppTheme


class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        setContent {
            MovieAppTheme {
                HomeNavHost(
                    onOpenSignIn = {
                        val intent = Intent(
                            this@HomeActivity,
                            AuthActivity::class.java,
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }

                        startActivity(intent)
                    },
                )
            }
        }
    }
}