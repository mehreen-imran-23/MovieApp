package com.example.movieapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import com.example.movieapp.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<HomeEvent>(replay = 0)
    val events = _events.asSharedFlow()

    init {
        loadHomeData()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.SearchClicked -> {


                sendEvent(HomeEvent.NavigateToSearch)
            }

            is HomeIntent.BookClicked -> {
                sendEvent(
                    HomeEvent.NavigateToMovieDetails(
                        movieId = intent.movieId,
                    )
                )
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMsg = null,
                )
            }

            try {
                val homeData = repository.getHomeData()

                _uiState.update {
                    it.copy(
                        userName = homeData.name.orEmpty(),
                        city = homeData.city.orEmpty(),
                        trendingMovies = homeData.trendingMovies,
                        recommendedMovies = homeData.recommendedMovies,
                        isLoading = false,
                    )
                }

            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = R.string.home_error,
                    )
                }
            }
        }
    }

    private fun sendEvent(event: HomeEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}