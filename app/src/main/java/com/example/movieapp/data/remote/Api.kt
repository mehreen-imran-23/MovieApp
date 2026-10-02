package com.example.movieapp.data.remote

import retrofit2.http.GET

interface Api {
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(): MoviesResponseDto

    @GET("genre/movie/list")
    suspend fun getMovieGenres(): GenresResponseDto
}