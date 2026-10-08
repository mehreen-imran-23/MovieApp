package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetailsDto(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("runtime")
    val runtime: Int?,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("genres")
    val genres: List<GenreDto>?,

    @SerializedName("spoken_languages")
    val spokenLanguages: List<MovieLanguageDto>?,

    @SerializedName("credits")
    val credits: MovieCreditsDto?,

    @SerializedName("release_dates")
    val releaseDates: MovieReleaseDatesDto?,
)

data class MovieLanguageDto(
    @SerializedName("english_name")
    val englishName: String?,

    @SerializedName("name")
    val name: String?,
)

data class MovieCreditsDto(
    @SerializedName("cast")
    val cast: List<MovieCastDto>?,
)

data class MovieCastDto(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("character")
    val character: String?,

    @SerializedName("profile_path")
    val profilePath: String?,
)

data class MovieReleaseDatesDto(
    @SerializedName("results")
    val results: List<CountryRelease>?,
)

data class CountryRelease(
    @SerializedName("iso_3166_1")
    val countryCode: String?,

    @SerializedName("release_dates")
    val releaseDates: List<MovieCertificationDto>?,
)

data class MovieCertificationDto(
    @SerializedName("certification")
    val certification: String?,

    @SerializedName("type")
    val type: Int?,
)