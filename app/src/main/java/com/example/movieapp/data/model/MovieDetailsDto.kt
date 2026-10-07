package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetailsDto(
    val id: Int?,
    val title: String?,
    val overview: String?,
    val runtime: Int?,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    val genres: List<GenreDto>?,

    @SerializedName("spoken_languages")
    val spokenLanguages: List<MovieLanguageDto>?,

    val credits: MovieCreditsDto?,

    @SerializedName("release_dates")
    val releaseDates: MovieReleaseDatesDto?,
)

data class MovieLanguageDto(
    @SerializedName("english_name")
    val englishName: String?,
    val name: String?,
)

data class MovieCreditsDto(val cast: List<MovieCastDto>?)
data class MovieCastDto(
    val id: Int?,
    val name: String?,
    val character: String?,
    @SerializedName("profile_path")
    val profilePath: String?,
)

data class MovieReleaseDatesDto(val results: List<CountryRelease>?)
data class CountryRelease(
    @SerializedName("iso_3166_1")
    val countryCode: String?,

    @SerializedName("release_dates")
    val releaseDates: List<MovieCertificationDto>?,
)

data class MovieCertificationDto(
    val certification: String?,
    val type: Int?,
)