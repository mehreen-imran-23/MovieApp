package com.example.movieapp.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.feature.splash.SplashEvent
import com.example.movieapp.feature.splash.SplashScreen
import com.example.movieapp.feature.splash.SplashViewModel

@Composable
fun OnboardingNavHost(
    versionName: String,
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Destinations.Splash.route,
        enterTransition = {
            EnterTransition.None
        },

        exitTransition = {
            fadeOut(
                animationSpec = tween(700)
            )
        } ,
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(Destinations.Splash.route) {
            val viewModel: SplashViewModel = viewModel()
            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(viewModel, lifecycleOwner) {
                lifecycleOwner.lifecycle.repeatOnLifecycle(
                    Lifecycle.State.RESUMED
                ) {
                    viewModel.event.collect { event ->
                        when (event) {
                            SplashEvent.Navigate -> {
                                nav.navigate(
                                    Destinations.OnboardingPosters.route
                                ) {
                                    popUpTo(Destinations.Splash.route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            }

            SplashScreen(
                versionName = versionName,
            )
        }
        composable(Destinations.OnboardingPosters.route)
        {
        }
    }
}