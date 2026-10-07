package com.example.movieapp.data.repository

import com.example.movieapp.data.local.model.UserDao

class ProfileRepository(
    private val userDao: UserDao,
) {

    suspend fun saveProfile(
        userId: Long, name: String, phoneNumber: String, city: String,
    ): Boolean {
        return userDao.updateProfile(
            userId = userId,
            name = name.trim(),
            phoneNumber = phoneNumber.trim(),
            city = city.trim(),
        ) == 1
    }
}