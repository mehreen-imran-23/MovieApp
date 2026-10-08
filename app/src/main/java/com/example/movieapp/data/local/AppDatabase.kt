package com.example.movieapp.data.local

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.movieapp.data.model.SearchDao
import com.example.movieapp.data.model.UserDao

@Database(
    entities = [
        UserEntity::class,
        AppPreferencesEntity::class,
        RecentSearchEntity::class,
    ],
    version = 6,
    exportSchema = true,
)
@ColumnTypeConverters(GenreIdsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun SearchDao(): SearchDao
}

