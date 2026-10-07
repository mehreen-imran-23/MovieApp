package com.example.movieapp.data.repository

import com.example.movieapp.data.cleanData.HomeData
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.local.model.UserDao
import com.example.movieapp.data.mapper.HomeMapper
import com.example.movieapp.data.remote.Api
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class HomeRepository(
    private val api: Api,
    private val mapper: HomeMapper,
    private val userDao: UserDao,
    private val preferences: LocalPreferences,
) {
    private var preloadedData: HomeData? = null
    private var preloadedUserId: Long? = null

    suspend fun preloadHomeData() {
        preloadedData = null
        val userId = preferences.activeUserId.first()
        val data = fetchHomeData()
        preloadedUserId = userId
        preloadedData = data
    }

    suspend fun getHomeData(): HomeData {
        val userId = preferences.activeUserId.first()
        val data = preloadedData

        preloadedData = null

        if (data != null && userId == preloadedUserId) {
            return data
        }

        return fetchHomeData()
    }

    private suspend fun fetchHomeData(): HomeData = coroutineScope {
        val userId = preferences.activeUserId.first()
        val user = if (userId != null) {
            userDao.getUserById(userId)
        } else {
            null
        }

        val selectedGenreIds = if (user != null) {
            user.selectedGenreIds.toSet()
        } else {
            preferences.selectedGenreIds.first()
        }

        val genreFilter = if (selectedGenreIds.isEmpty()) {
            null
        } else {
            selectedGenreIds.sorted().joinToString("|")
        }

        val trendingRequest = async {
            api.getTrendingMovies()
        }

        val recommendedRequest = async {
            api.getRecommendedMovies(
                genreIds = genreFilter,
            )
        }

        val genresRequest = async {
            api.getMovieGenres()
        }

        val trendingResponse = trendingRequest.await()
        val recommendedResponse = recommendedRequest.await()
        val genres = genresRequest.await().genres.orEmpty()

        HomeData(
            name = user?.name.orEmpty(),
            city = user?.city.orEmpty(),
            trendingMovies = mapper.mapMovies(
                movies = trendingResponse.results.orEmpty(),
                genres = genres,
            ),
            recommendedMovies = mapper.mapMovies(
                movies = recommendedResponse.results.orEmpty(),
                genres = genres,
            ),
        )
    }
}