package com.example.movieapp.data.model

data class SearchPage(
    val movies: List<HomeMovie>,
    val page: Int,
    val totalPages: Int,
)