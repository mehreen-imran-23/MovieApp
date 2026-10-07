package com.example.movieapp.data.mapper

import com.example.movieapp.data.cleanData.HomeMovie
import com.example.movieapp.data.remote.GenreDto
import com.example.movieapp.data.remote.MovieDto
import java.util.Locale

class HomeMapper {

    fun mapMovies(
        movies: List<MovieDto>,
        genres: List<GenreDto>,
    ): List<HomeMovie> {
        val genreNames = mutableMapOf<Int, String>()

        for (genre in genres) {
            val id = genre.id
            val name = genre.name

            if (id != null && !name.isNullOrBlank()) {
                genreNames[id] = name
            }
        }

        val homeMovies = mutableListOf<HomeMovie>()

        for (movie in movies) {
            val id = movie.id
            val title = movie.title

            if (id == null || title.isNullOrBlank()) {
                continue
            }

            val posterPath = movie.posterPath
            val posterUrl: String?

            if (posterPath.isNullOrBlank()) {
                posterUrl = null
            } else {
                posterUrl = "https://image.tmdb.org/t/p/w500$posterPath"
            }

            val languageCode = movie.originalLanguage
            val language: String

            if (languageCode.isNullOrBlank()) {
                language = ""
            } else {
                language = Locale.forLanguageTag(languageCode)
                    .getDisplayLanguage(Locale.ENGLISH)
            }

            val movieGenres = mutableListOf<String>()

            for (genreId in movie.genreIds.orEmpty()) {
                val genreName = genreNames[genreId]

                if (genreName != null) {
                    movieGenres.add(genreName)
                }
            }

            homeMovies.add(
                HomeMovie(
                    id = id,
                    title = title,
                    posterUrl = posterUrl,
                    language = language,
                    genres = movieGenres,
                    certification = null,
                )
            )
        }

        return homeMovies.distinctBy { it.id }
    }
}