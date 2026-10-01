package com.example.movieapp.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "app_preferences")
data class AppPreferencesEntity(
    @PrimaryKey
    val id: Int = 1,
    val selectedGenreIds: List<Int> = emptyList(),
)