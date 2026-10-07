package com.example.movieapp.data.cleanData

data class HomeData(
    val name: String?,
    val city: String?,
    val trendingMovies: List<HomeMovie>,
    val recommendedMovies: List<HomeMovie>,  //entre home page data
)