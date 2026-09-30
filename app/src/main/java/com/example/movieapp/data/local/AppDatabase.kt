package com.example.movieapp.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun userDao(): UserDao
}