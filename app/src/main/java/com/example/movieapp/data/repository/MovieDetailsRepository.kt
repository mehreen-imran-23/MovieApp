package com.example.movieapp.data.repository

import com.example.movieapp.data.cleanData.MovieDetailsData
import com.example.movieapp.data.mapper.MovieDetailsMapper
import com.example.movieapp.data.remote.Api

class MovieDetailsRepository(
    private val api: Api,
    private val mapper: MovieDetailsMapper,
) {
    suspend fun getMovieDetails(movieId: Int, countryCode: String): MovieDetailsData {
        val movie = api.getMovieDetails(
            movieId = movieId,
        )

        return mapper.mapMovie(movie = movie, countryCode = countryCode)
    }
}