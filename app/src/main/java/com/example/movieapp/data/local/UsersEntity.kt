package com.example.movieapp.data.local

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(
            value = ["email"],
            unique = true,
        ),
    ],
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val passwordHash: String,
    val passwordSalt: String,
    @ColumnInfo(defaultValue = "''")
    val selectedGenreIds: List<Int> = emptyList(),

    @ColumnInfo(defaultValue = "''")
    val name: String = "",

    @ColumnInfo(defaultValue = "''")
    val phoneNumber: String = "",

    @ColumnInfo(defaultValue = "''")
    val city: String = "",
)