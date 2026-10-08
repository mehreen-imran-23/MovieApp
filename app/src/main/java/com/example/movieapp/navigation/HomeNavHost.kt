package com.example.movieapp.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieapp.R
import com.example.movieapp.feature.home.HomeEvent
import com.example.movieapp.feature.home.HomeScreen
import com.example.movieapp.feature.home.HomeViewModel
import com.example.movieapp.feature.moviedetails.MovieDetailsEvent
import com.example.movieapp.feature.moviedetails.MovieDetailsScreen
import com.example.movieapp.feature.moviedetails.MovieDetailsViewModel
import com.example.movieapp.feature.search.SearchEvent
import com.example.movieapp.feature.search.SearchScreen
import com.example.movieapp.feature.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun HomeNavHost(
    onOpenSignIn: () -> Unit,
) {
    val nav = rememberNavController()
    val context = LocalContext.current


    NavHost(
        navController = nav,
        startDestination = "home",
    ) {
        composable("home") { entry ->
            val viewModel: HomeViewModel = koinViewModel()

            LaunchedEffect(viewModel, entry, onOpenSignIn, context) {
                entry.lifecycle.repeatOnLifecycle(
                    Lifecycle.State.STARTED
                ) {
                    viewModel.events.collect { event ->
                        when (event) {
                            HomeEvent.NavigateToSearch -> {
                                nav.navigate("search") {
                                    launchSingleTop = true
                                }
                            }

                            HomeEvent.NavigateToSignIn -> {
                                onOpenSignIn()
                            }

                            HomeEvent.LogoutFailed -> {
                                Toast.makeText(
                                    context,
                                    R.string.logout_failed,
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }

                            is HomeEvent.NavigateToMovieDetails -> {
                                nav.navigate("movie_details/${event.movieId}") {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            }

            HomeScreen(
                viewModel = viewModel,
            )
        }

        composable("search") { entry ->
            val viewModel: SearchViewModel = koinViewModel()

            LaunchedEffect(viewModel, entry) {
                entry.lifecycle.repeatOnLifecycle(
                    Lifecycle.State.STARTED
                ) {
                    viewModel.events.collect { event ->
                        when (event) {
                            SearchEvent.NavigateBack -> {
                                nav.popBackStack()
                            }

                            is SearchEvent.NavigateToMovieDetails -> {
                                nav.navigate("movie_details/${event.movieId}") {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            }

            SearchScreen(
                viewModel = viewModel,
            )
        }

        composable(
            route = "movie_details/{movieId}",
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                }
            ),
        ) { entry ->
            val movieId = requireNotNull(entry.arguments).getInt("movieId")

            val viewModel: MovieDetailsViewModel = koinViewModel(
                viewModelStoreOwner = entry,
                parameters = {
                    parametersOf(movieId, "IN")
                },
            )

            LaunchedEffect(viewModel, entry) {
                entry.lifecycle.repeatOnLifecycle(
                    Lifecycle.State.STARTED
                ) {
                    viewModel.events.collect { event ->
                        when (event) {
                            MovieDetailsEvent.NavigateBack -> {
                                nav.popBackStack()
                            }
                        }
                    }
                }
            }

            MovieDetailsScreen(
                viewModel = viewModel,
            )
        }
    }
}