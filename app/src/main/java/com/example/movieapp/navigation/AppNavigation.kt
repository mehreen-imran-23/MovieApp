package com.example.movieapp.navigation

import Onboarding1Event
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.feature.auth.AuthEnum
import com.example.movieapp.feature.auth.AuthEvent
import com.example.movieapp.feature.auth.AuthViewModel
import com.example.movieapp.feature.auth.signin.SignInScreen
import com.example.movieapp.feature.auth.signup.SignUpScreen
import com.example.movieapp.feature.onboarding.OnboardingScreen
import com.example.movieapp.feature.onboarding.OnboardingViewModel
import com.example.movieapp.feature.splash.SplashEvent
import com.example.movieapp.feature.splash.SplashScreen
import com.example.movieapp.feature.splash.SplashViewModel
import kotlinx.coroutines.flow.first
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun OnboardingNavHost(
    versionName: String,
    onOpenSignIn: (Boolean) -> Unit,
    onOpenHome: () -> Unit,
) {
    val nav = rememberNavController()
    val onboardingViewModel: OnboardingViewModel = koinViewModel()


    NavHost(
        navController = nav,
        startDestination = Destinations.Splash.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {

        composable(Destinations.Splash.route) { entry ->

            val splashViewModel: SplashViewModel = koinViewModel()

            LaunchedEffect(
                splashViewModel,
                entry,
                nav,
                onOpenSignIn,
                onOpenHome,
            ) {
                entry.lifecycle.repeatOnLifecycle(
                    Lifecycle.State.RESUMED,
                ) {
                    splashViewModel.event.collect { event ->

                        if (
                            nav.currentDestination?.route ==
                            Destinations.Splash.route
                        ) {
                            when (event) {

                                SplashEvent.Navigate -> {
                                    onboardingViewModel.uiState.first { state ->
                                        !state.isLoading
                                    }

                                    nav.navigate(
                                        Destinations.OnboardingPosters.route
                                    ) {
                                        popUpTo(Destinations.Splash.route) {
                                            inclusive = true
                                        }

                                        launchSingleTop = true
                                    }
                                }

                                SplashEvent.NavigateToSignIn -> {
                                    onOpenSignIn(true)
                                }

                                SplashEvent.NavigateToHome -> {
                                    onOpenHome()
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

        composable(Destinations.OnboardingPosters.route) { entry ->
            LaunchedEffect(
                onboardingViewModel,
                entry,
                onOpenSignIn,
            ) {
                entry.lifecycle.repeatOnLifecycle(
                    Lifecycle.State.RESUMED,
                ) {
                    onboardingViewModel.events.collect { event ->

                        when (event) {
                            Onboarding1Event.NavigateToSignIn -> {
                                onOpenSignIn(false)
                            }

                            Onboarding1Event.NavigateToHome -> {
                                onOpenHome()
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

@Composable
fun AuthNavHost(
    onOpenHome: () -> Unit,
    onOpenProfile: () -> Unit,
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = AuthEnum.SignIn.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {

        for (mode in AuthEnum.entries) {

            composable(route = mode.name) { entry ->

                val authViewModel: AuthViewModel = koinViewModel(
                    viewModelStoreOwner = entry,
                    parameters = {
                        parametersOf(mode)
                    },
                )

                LaunchedEffect(
                    authViewModel,
                    entry,
                    nav,
                    onOpenHome,
                    onOpenProfile,
                ) {
                    entry.lifecycle.repeatOnLifecycle(
                        Lifecycle.State.RESUMED,
                    ) {
                        authViewModel.events.collect { event ->

                            when (event) {

                                AuthEvent.NavigateToSignUp -> {

                                    if (
                                        nav.currentDestination?.route ==
                                        AuthEnum.SignIn.name
                                    ) {
                                        nav.navigate(
                                            AuthEnum.SignUp.name
                                        ) {
                                            launchSingleTop = true
                                        }
                                    }
                                }

                                AuthEvent.NavigateToSignIn -> {

                                    if (
                                        nav.currentDestination?.route ==
                                        AuthEnum.SignUp.name
                                    ) {
                                        nav.popBackStack(
                                            route = AuthEnum.SignIn.name,
                                            inclusive = false,
                                        )
                                    }
                                }

                                AuthEvent.NavigateToHome -> {
                                    onOpenHome()
                                }

                                AuthEvent.NavigateToProfile -> {
                                    onOpenProfile()
                                }

                                else -> Unit
                            }
                        }
                    }
                }

                when (mode) {

                    AuthEnum.SignIn -> {
                        SignInScreen(
                            viewModel = authViewModel,
                        )
                    }

                    AuthEnum.SignUp -> {
                        SignUpScreen(
                            viewModel = authViewModel,
                        )
                    }
                }
            }
        }
    }
}