package com.example.movieapp.feature.moviedetails

import androidx.annotation.StringRes
import com.example.movieapp.data.cleanData.MovieDetailsData

data class MovieDetailsState(
    val movie: MovieDetailsData? = null,
    val isLoading: Boolean = false,
    @param:StringRes val errorMsg: Int? = null,
)