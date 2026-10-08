package com.example.movieapp.data.model

data class MovieDetailsData(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val genres: List<String>,
    val durationMinutes: Int?,
    val releaseDate: String?,
    val languages: List<String>,
    val overview: String,
    val certification: String?,
    val certificationCountry: String?,
    val cast: List<MovieCast>,
)

data class MovieCast(
    val id: Int,
    val name: String,
    val character: String,
    val imageUrl: String?,
)