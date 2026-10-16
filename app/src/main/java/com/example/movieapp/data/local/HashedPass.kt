package com.example.movieapp.data.local

data class HashedPass(
    val hash: String,
    val salt: String,
)