
package com.example.movieapp.feature.moviedetails
sealed interface MovieDetailsIntent {
    data object BackClicked : MovieDetailsIntent
}