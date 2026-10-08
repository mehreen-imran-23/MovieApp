package com.example.movieapp.data.model

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.example.movieapp.data.local.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query(
        """
        SELECT * FROM recent_movies
        WHERE ownerKey = :ownerKey
        ORDER BY searchedAt DESC, movieId DESC
        """
    )
    fun observeSearches(
        ownerKey: String,
    ): Flow<List<RecentSearchEntity>>

    @Upsert
    suspend fun saveSearch(
        movies: List<RecentSearchEntity>,
    )
}