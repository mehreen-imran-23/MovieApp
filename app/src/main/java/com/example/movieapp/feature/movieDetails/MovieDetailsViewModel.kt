package com.example.movieapp.feature.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import com.example.movieapp.data.repository.MovieDetailsRepository
import com.example.movieapp.feature.movieDetails.MovieDetailsIntent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val repository: MovieDetailsRepository,
    private val movieId: Int,
    private val countryCode: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieDetailsState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MovieDetailsEvent>()
    val events = _events.asSharedFlow()

    init {
        loadMovieDetails()
    }

    fun onIntent(intent: MovieDetailsIntent) {
        when (intent) {
            MovieDetailsIntent.BackClicked -> {
                viewModelScope.launch {
                    _events.emit(MovieDetailsEvent.NavigateBack)
                }
            }
        }
    }

    private fun loadMovieDetails() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    errorMsg = null,
                )
            }

            try {
                val movie = repository.getMovieDetails(
                    movieId = movieId,
                    countryCode = countryCode,
                )

                _uiState.update { state ->
                    state.copy(
                        movie = movie,
                        isLoading = false,
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMsg = R.string.movie_details_failed,
                    )
                }
            }
        }
    }
}