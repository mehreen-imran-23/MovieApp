package com.example.movieapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore(
    name = "genrePreferences",
)

class LocalPreferences(
    context: Context,
    private val database: AppDatabase,
) {
    private val dataStore = context.applicationContext.sessionDataStore
    private val userDao = database.userDao()
    private val userIdKey = longPreferencesKey("userId")
    private val guestKey = booleanPreferencesKey("isGuest")
    private val onboardingKey =
        booleanPreferencesKey("onboardingCompleted")

    val selectedGenreIds: Flow<Set<Int>> =
        userDao.observePreferences().map { preferences ->
            preferences?.selectedGenreIds.orEmpty().toSet()
        }

    val activeUserId: Flow<Long?> =
        dataStore.data.map { preferences ->
            preferences[userIdKey]
        }

    val isGuest: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[guestKey] ?: false
        }

    val onboardingCompleted: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[onboardingKey] ?: false
        }

    suspend fun saveSelectedGenres(ids: Set<Int>) {
        userDao.savePreferences(
            AppPreferencesEntity(
                selectedGenreIds = ids.toList(),
            )
        )

        dataStore.edit { preferences ->
            preferences[onboardingKey] = true
        }
    }

    suspend fun saveUserSession(userId: Long) {
        dataStore.edit { preferences ->
            preferences[userIdKey] = userId
            preferences.remove(guestKey)
            preferences[onboardingKey] = true
        }
    }

    suspend fun saveGuest() {
        dataStore.edit { preferences ->
            preferences.remove(userIdKey)
            preferences[guestKey] = true
            preferences[onboardingKey] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(userIdKey)
            preferences.remove(guestKey)
        }
    }
}