package com.example.movieapp.data.remote

import com.google.gson.annotations.SerializedName

data class MoviesResponseDto(
    @SerializedName("results")
    val results: List<MovieDto>? = null,
)

data class MovieDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,
)

data class GenresResponseDto(
    @SerializedName("genres")
    val genres: List<GenreDto>? = null,
)

data class GenreDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null,
)