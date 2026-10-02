package com.example.movieapp.data.local

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        AppPreferencesEntity::class,
    ],
    version = 4,
    exportSchema = true,
)
@ColumnTypeConverters(GenreIdsConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}