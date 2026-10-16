package com.example.movieapp.data.local

import androidx.room3.Entity

@Entity(
    tableName = "recent_movies",
    primaryKeys = ["ownerKey", "movieId"],
)
data class RecentSearchEntity(
    val ownerKey: String,
    val movieId: Int,
    val title: String,
    val posterUrl: String?,
    val language: String,
    val searchedAt: Long,
)