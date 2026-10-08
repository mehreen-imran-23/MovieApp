package com.example.movieapp.data.remote

import com.google.gson.annotations.SerializedName

data class MoviesResponseDto(
    @SerializedName("results")
    val results: List<MovieDto>? = null,

    @SerializedName("page")
    val page: Int = 1,

    @SerializedName("total_pages")
    val totalPages: Int = 0,
)

data class MovieDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("original_language")
    val originalLanguage: String? = null,

    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null,
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