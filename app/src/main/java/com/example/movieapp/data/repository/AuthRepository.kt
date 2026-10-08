package com.example.movieapp.data.repository

import com.example.movieapp.data.local.PassHash
import com.example.movieapp.data.local.UserEntity
import com.example.movieapp.data.model.UserDao

class AuthRepository(
    private val userDao: UserDao,
    private val passwHash: PassHash,
) {

    suspend fun signUp(
        email: String, password: String, selectedGenreIds: Set<Int>,
    ): AuthResult {
        val email = email.trim().lowercase()

        if (userDao.getUserByEmail(email) != null) {
            return AuthResult.EmailAlreadyExists
        }

        val hashed = passwHash.hash(password)

        val userId = userDao.insertUser(
            UserEntity(
                email = email,
                passwordHash = hashed.hash,
                passwordSalt = hashed.salt,
                selectedGenreIds = selectedGenreIds.toList(),
            )
        )

        return AuthResult.Success(
            userId = userId,
            email = email,
        )
    }

    suspend fun signIn(email: String, password: String): AuthResult {
        val email = email.trim().lowercase()
        val user = userDao.getUserByEmail(email) ?: return AuthResult.InvalidCredentials

        if (!passwHash.verifyPassword(
                password = password, savedHash = user.passwordHash,
                savedSalt = user.passwordSalt,
            )
        ) {
            return AuthResult.InvalidCredentials
        }

        return AuthResult.Success(
            userId = user.id, email = user.email,
        )
    }
}