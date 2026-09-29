package com.example.movieapp.navigation

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
import com.example.movieapp.feature.onboarding.Onboarding1Event
import com.example.movieapp.feature.onboarding.OnboardingScreen
import com.example.movieapp.feature.onboarding.OnboardingViewModel
import com.example.movieapp.feature.splash.SplashEvent
import com.example.movieapp.feature.splash.SplashScreen
import com.example.movieapp.feature.splash.SplashViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingNavHost(
    versionName: String,
    onOpenSignIn: () -> Unit,
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Destinations.Splash.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(Destinations.Splash.route) {
            val splashViewModel: SplashViewModel = viewModel()
            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(splashViewModel, lifecycleOwner) {
                lifecycleOwner.lifecycle.repeatOnLifecycle(
                    Lifecycle.State.RESUMED,
                ) {
                    splashViewModel.event.collect { event ->
                        when (event) {
                            SplashEvent.Navigate -> {
                                nav.navigate(
                                    Destinations.OnboardingPosters.route,
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

        composable(Destinations.OnboardingPosters.route) {
            val onboardingViewModel: OnboardingViewModel = koinViewModel()
            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(
                onboardingViewModel,
                lifecycleOwner,
                onOpenSignIn,
            ) {
                lifecycleOwner.lifecycle.repeatOnLifecycle(
                    Lifecycle.State.RESUMED,
                ) {
                    onboardingViewModel.events.collect { event ->
                        when (event) {
                            Onboarding1Event.NavigateToSignIn -> {
                                onOpenSignIn()
                            }
                        }
                    }
                }
            }

            OnboardingScreen(
                viewModel = onboardingViewModel,
            )
        }
    }
}