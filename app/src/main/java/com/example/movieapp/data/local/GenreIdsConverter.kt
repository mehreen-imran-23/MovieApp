package com.example.movieapp.data.local

import androidx.room3.ColumnTypeConverter

class GenreIdsConverter {

    @ColumnTypeConverter
    fun toDatabase(ids: List<Int>): String {
        return ids.distinct().sorted().joinToString(",")
    }

    @ColumnTypeConverter
    fun fromDatabase(value: String): List<Int> {
        if (value.isBlank())
        {
            return emptyList()
        }

        return value.split(",").map { it.toInt() }
    }
}