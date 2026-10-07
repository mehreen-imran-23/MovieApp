package com.example.movieapp.feature.home

import androidx.annotation.StringRes
import com.example.movieapp.data.cleanData.HomeMovie

data class HomeState(
    val userName: String = "",
    val city: String = "",
    val trendingMovies: List<HomeMovie> = emptyList(),
    val recommendedMovies: List<HomeMovie> = emptyList(),
    val isLoading: Boolean = false,
    @param:StringRes val errorMsg: Int? = null,
)