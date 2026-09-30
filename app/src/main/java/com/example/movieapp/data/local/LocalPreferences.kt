package com.example.movieapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.localDataStore by preferencesDataStore(
    name = "genrePreferences"
)

class LocalPreferences(
    context: Context
) {
    private val dataStore = context.applicationContext.localDataStore
    private val selectedGenresKey = stringSetPreferencesKey("selectedGenreIds")
    private val userIdKey = longPreferencesKey("userId")
    private val guestKey = booleanPreferencesKey("isGuest")

    val selectedGenreIds: Flow<Set<Int>> =
        dataStore.data.map { preferences ->
            val savedGenres = preferences[selectedGenresKey]
            if (savedGenres != null)
            {
                savedGenres.mapNotNull { genreId ->
                    genreId.toIntOrNull()
                }.toSet()
            }
            else
            {
                emptySet()
            }
        }

    val session: Flow<UserSession> =
        dataStore.data.map { preferences ->

            val userId = preferences[userIdKey]
            val isGuest = preferences[guestKey]

            if (userId != null) {
                UserSession.LoggedIn(userId)
            }
            else if (isGuest == true)
            {
                UserSession.Guest
            }
            else
            {
                UserSession.SignedOut
            }
        }


    suspend fun saveSelectedGenres(ids: Set<Int>) {

        val genreIdsAsStrings = ids.map { genreId ->
            genreId.toString()
        }.toSet()

        dataStore.edit { preferences ->
            preferences[selectedGenresKey] = genreIdsAsStrings
        }
    }


    suspend fun saveLogin(userId: Long) {

        dataStore.edit { preferences ->
            preferences[userIdKey] = userId
            preferences.remove(guestKey)
        }
    }


    suspend fun saveGuest() {

        dataStore.edit { preferences ->
            preferences.remove(userIdKey)
            preferences[guestKey] = true
        }
    }
}