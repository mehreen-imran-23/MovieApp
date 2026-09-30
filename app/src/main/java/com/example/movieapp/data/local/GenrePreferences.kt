package com.example.movieapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.genreDataStore by preferencesDataStore(
    name = "genrePreferences",
)

class GenrePreferences(
    context: Context,
) {
    private val dataStore = context.applicationContext.genreDataStore
    private val selectedKey =
        stringSetPreferencesKey("selectedGenreIds")

    val selectedGenreIds: Flow<Set<Int>> = dataStore.data.map { preferences ->
        preferences[selectedKey]
            .orEmpty()
            .mapNotNull { it.toIntOrNull() }
            .toSet()
    }

    suspend fun saveSelectedGenres(ids: Set<Int>) {
        dataStore.edit { preferences ->
            preferences[selectedKey] =
                ids.map { it.toString() }.toSet()
        }
    }

}