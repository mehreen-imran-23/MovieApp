package com.example.movieapp.data.mapper

import com.example.movieapp.data.model.MovieCast
import com.example.movieapp.data.model.MovieDetailsData
import com.example.movieapp.data.remote.model.MovieDetailsDto

class MovieDetailsMapper {
    fun mapMovie(
        movie: MovieDetailsDto, countryCode: String,
    ): MovieDetailsData {
        val genres = movie.genres.orEmpty().mapNotNull { genre ->
            genre.name?.takeIf { it.isNotBlank() }
        }

        val languages = movie.spokenLanguages.orEmpty().mapNotNull { lang ->
            when {
                !lang.englishName.isNullOrBlank() -> lang.englishName
                else -> lang.name?.takeIf { it.isNotBlank() }
            }
        }

        val cast = movie.credits?.cast.orEmpty().mapNotNull { actor ->
            val id = actor.id
            val name = actor.name

            when {
                id == null || name.isNullOrBlank() -> null
                else -> MovieCast(
                    id = id,
                    name = name,
                    character = actor.character.orEmpty(),
                    imageUrl = imageUrl(actor.profilePath),
                )
            }
        }

        val certification = getCertification(movie, countryCode)

        return MovieDetailsData(
            id = requireNotNull(movie.id),
            title = movie.title.orEmpty(),
            posterUrl = imageUrl(movie.posterPath),
            genres = genres,
            durationMinutes = movie.runtime?.takeIf { it > 0 },
            releaseDate = movie.releaseDate?.takeIf { it.isNotBlank() },
            languages = languages,
            overview = movie.overview.orEmpty(),
            certification = certification,
            certificationCountry = when {
                certification != null -> countryCode
                else -> null
            },
            cast = cast,
        )
    }

    private fun getCertification(
        movie: MovieDetailsDto, countryCode: String,
    ): String? {
        val country = movie.releaseDates?.results.orEmpty().firstOrNull {
            it.countryCode.equals(countryCode, ignoreCase = true)
        }

        val release = country?.releaseDates.orEmpty().firstOrNull {
            it.type == 3 && !it.certification.isNullOrBlank()
        }
        return release?.certification
    }

    private fun imageUrl(path: String?): String? {
        return when {
            path.isNullOrBlank() -> null
            else -> "https://image.tmdb.org/t/p/w500$path"
        }
    }
}