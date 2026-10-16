package com.example.movieapp.navigation

sealed class Destinations(val route: String) {

    data object Splash : Destinations("splash")

    data object OnboardingPosters : Destinations("onboardingPosters")
    data object OnboardingGenres : Destinations("onboardingGenres")
    data object SignIn : Destinations("signIn")
    data object SignUp : Destinations("signUp")
    data object ProfileSetup : Destinations("profileSetup")
    data object Home : Destinations("home")
    data object RecentSearch : Destinations("recentSearch")
    data object SearchResults : Destinations("searchResults")
    data object MovieDetails : Destinations("movieDetails")
}