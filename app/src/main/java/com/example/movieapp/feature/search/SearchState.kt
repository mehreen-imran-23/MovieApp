package com.example.movieapp.feature.search

import androidx.annotation.StringRes
import com.example.movieapp.data.cleanData.HomeMovie

data class SearchUiState(
    val query: String = "",
    val recentSearches: List<String> = emptyList(),
    val movies: List<HomeMovie> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val recentMovies: List<HomeMovie> = emptyList(),
    @param:StringRes val errorMsg: Int? = null,
    val isLoadingMore: Boolean = false,
    val NextPage: Boolean = false,
    @param:StringRes val loadMoreError: Int? = null,
)