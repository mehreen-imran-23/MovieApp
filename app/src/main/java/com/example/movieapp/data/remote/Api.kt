package com.example.movieapp.data.remote

import com.example.movieapp.data.remote.model.MovieDetailsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(): MoviesResponseDto

    @GET("genre/movie/list")
    suspend fun getMovieGenres(): GenresResponseDto

    @GET("discover/movie")
    suspend fun getRecommendedMovies(
        @Query("with_genres") genreIds: String? = null,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("page") page: Int = 1,
    ): MoviesResponseDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
    ): MoviesResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response")
        appendToResponse: String = "credits,release_dates",
    ): MovieDetailsDto
}

