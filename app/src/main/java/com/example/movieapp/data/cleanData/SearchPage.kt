package com.example.movieapp.data.cleanData

data class SearchPage(
    val movies: List<HomeMovie>,
    val page: Int,
    val totalPages: Int,
)