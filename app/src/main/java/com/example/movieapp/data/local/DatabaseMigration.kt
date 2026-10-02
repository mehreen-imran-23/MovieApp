package com.example.movieapp.data.local

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val migration_2_3 = object : Migration(2, 3) {

    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS app_preferences (
                id INTEGER NOT NULL,
                selectedGenreIds TEXT NOT NULL,
                PRIMARY KEY(id)
            )
            """.trimIndent()
        )
    }
}

val migration_3_4 = object : Migration(3, 4) {

    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "ALTER TABLE users ADD COLUMN name TEXT NOT NULL DEFAULT ''"
        )
        connection.execSQL(
            "ALTER TABLE users ADD COLUMN phoneNumber TEXT NOT NULL DEFAULT ''"
        )
        connection.execSQL(
            "ALTER TABLE users ADD COLUMN city TEXT NOT NULL DEFAULT ''"
        )
    }
}