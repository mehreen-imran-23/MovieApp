package com.example.movieapp.feature.search

sealed interface SearchIntent {

    data class QueryChanged(val query: String) : SearchIntent
    data object SearchClicked : SearchIntent
    data class RecentSearchClicked(val query: String) : SearchIntent
    data class MovieClicked(val movieId: Int) : SearchIntent
    data object BackClicked : SearchIntent
    data object LoadMore : SearchIntent
}