package com.example.movieapp.data.mapper

import com.example.movieapp.data.cleanData.MovieCast
import com.example.movieapp.data.cleanData.MovieDetailsData
import com.example.movieapp.data.model.MovieDetailsDto

class MovieDetailsMapper {
    fun mapMovie(
        movie: MovieDetailsDto,
        countryCode: String,
    ): MovieDetailsData {
        val genres = mutableListOf<String>()
        val languages = mutableListOf<String>()
        val cast = mutableListOf<MovieCast>()

        for (genre in movie.genres.orEmpty()) {
            val name = genre.name

            if (!name.isNullOrBlank()) {
                genres.add(name)
            }
        }

        for (lang in movie.spokenLanguages.orEmpty()) {
            val englishName = lang.englishName
            val name = lang.name

            if (!englishName.isNullOrBlank()) {
                languages.add(englishName)
            } else if (!name.isNullOrBlank()) {
                languages.add(name)
            }
        }

        for (actor in movie.credits?.cast.orEmpty()) {
            val id = actor.id
            val name = actor.name

            if (id != null && !name.isNullOrBlank()) {
                cast.add(
                    MovieCast(
                        id = id,
                        name = name,
                        character = actor.character.orEmpty(),
                        imageUrl = imageUrl(actor.profilePath),
                    )
                )
            }
        }

        var certification: String? = null

        for (country in movie.releaseDates?.results.orEmpty()) {
            if (country.countryCode.equals(countryCode, ignoreCase = true)) {
                for (release in country.releaseDates.orEmpty()) {
                    if (
                        release.type == 3 &&
                        !release.certification.isNullOrBlank()
                    ) {
                        certification = release.certification
                        break
                    }
                }

                break
            }
        }

        val runtime = movie.runtime

        return MovieDetailsData(
            id = requireNotNull(movie.id),
            title = movie.title.orEmpty(),
            posterUrl = imageUrl(movie.posterPath),
            genres = genres,
            durationMinutes = if (runtime != null && runtime > 0) {
                runtime
            } else {
                null
            },
            releaseDate = if (movie.releaseDate.isNullOrBlank()) {
                null
            } else {
                movie.releaseDate
            },
            languages = languages,
            overview = movie.overview.orEmpty(),
            certification = certification,
            certificationCountry = if (certification != null) {
                countryCode
            } else {
                null
            },
            cast = cast,
        )
    }

    private fun imageUrl(path: String?): String? {
        if (path.isNullOrBlank()) {
            return null
        } else {
            return "https://image.tmdb.org/t/p/w500$path"
        }
    }
}