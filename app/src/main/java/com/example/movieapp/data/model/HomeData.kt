package com.example.movieapp.data.model

data class HomeData(
    val name: String?,
    val city: String?,
    val trendingMovies: List<HomeMovie>,
    val recommendedMovies: List<HomeMovie>,  //entre home page data
)