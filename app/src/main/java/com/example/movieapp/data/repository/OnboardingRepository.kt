package com.example.movieapp.data.repository

import com.example.movieapp.data.remote.Api
import java.io.IOException

data class Genre(
    val id: Int,
    val name: String,
)

class OnboardingRepository(
    private val api: Api,
) {
    suspend fun getPosterUrls(): List<String> {
        val posters = api.getTrendingMovies()
            .results
            .orEmpty()
            .mapNotNull { movie ->
                movie.posterPath
                    ?.takeIf { it.isNotBlank() }
                    ?.let { path ->
                        "https://image.tmdb.org/t/p/w500$path"
                    }
            }
            .distinct()
            .take(8)

        if (posters.isEmpty()) {
            throw IOException("No posters returned")
        }

        return posters
    }

    suspend fun getGenres(): List<Genre> {
        val genres = api.getMovieGenres()
            .genres
            .orEmpty()
            .mapNotNull { genre ->
                val id = genre.id
                val name = genre.name

                if (id != null && !name.isNullOrBlank()) {
                    Genre(id = id, name = name)
                } else {
                    null
                }
            }
            .distinctBy { it.id }

        if (genres.isEmpty()) {
            throw IOException("No genres returned")
        }
        return genres
    }
}