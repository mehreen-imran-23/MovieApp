package com.example.movieapp.data.repository

import com.example.movieapp.data.cleanData.HomeMovie
import com.example.movieapp.data.cleanData.SearchPage
import com.example.movieapp.data.local.RecentSearchEntity
import com.example.movieapp.data.local.model.SearchDao
import com.example.movieapp.data.mapper.HomeMapper
import com.example.movieapp.data.remote.Api
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchRepository(
    private val api: Api, private val mapper: HomeMapper,
    private val recentSearchDao: SearchDao,
) {

    fun observeSearches(userId: Long?): Flow<List<HomeMovie>> {
        return recentSearchDao.observeSearches(ownerKey = getOwnerKey(userId))
            .map { searches ->
                searches.map { search ->
                    HomeMovie(
                        id = search.movieId,
                        title = search.title,
                        posterUrl = search.posterUrl,
                        language = search.language,
                    )
                }
            }
    }

    suspend fun saveSearch(
        userId: Long?,
        movies: List<HomeMovie>,
    ) {
        if (movies.isEmpty()) {
            return
        }

        val ownerKey = getOwnerKey(userId)
        val now = System.currentTimeMillis()
        val searches = mutableListOf<RecentSearchEntity>()

        for ((index, movie) in movies.distinctBy { it.id }.withIndex()) {
            searches.add(
                RecentSearchEntity(
                    ownerKey = ownerKey,
                    movieId = movie.id,
                    title = movie.title,
                    posterUrl = movie.posterUrl,
                    language = movie.language,
                    searchedAt = now - index,
                )
            )
        }

        recentSearchDao.saveSearch(searches)
    }

    suspend fun searchMovies(
        query: String,
        page: Int = 1,
    ): SearchPage = coroutineScope {
        val searchQuery = query.trim()

        if (searchQuery.isBlank()) {
            return@coroutineScope SearchPage(
                movies = emptyList(),
                page = 1,
                totalPages = 0,
            )
        }

        val moviesReq = async {
            api.searchMovies(
                query = searchQuery,
                page = page,
            )
        }

        val genresReq = async {
            api.getMovieGenres()
        }

        val moviesResponse = moviesReq.await()
        val genresResponse = genresReq.await()

        SearchPage(
            movies = mapper.mapMovies(
                movies = moviesResponse.results.orEmpty(),
                genres = genresResponse.genres.orEmpty(),
            ),
            page = moviesResponse.page,
            totalPages = moviesResponse.totalPages,
        )
    }

    private fun getOwnerKey(userId: Long?): String {
        return if (userId == null) {
            "guest"
        } else {
            "user_$userId"
        }
    }
}