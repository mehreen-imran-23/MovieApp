package com.example.movieapp.data.repository

sealed interface AuthResult {
    data class Success(val userId: Long, val email: String, ) : AuthResult
    data object EmailAlreadyExists : AuthResult
    data object InvalidCredentials : AuthResult
}