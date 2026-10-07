package com.example.movieapp.data.cleanData

data class HomeMovie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val language: String,
    val genres: List<String> = emptyList(),
    val certification: String? = null,  //one movie data
)