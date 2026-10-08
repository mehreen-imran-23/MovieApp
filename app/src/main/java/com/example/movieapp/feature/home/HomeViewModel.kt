package com.example.movieapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.repository.HomeRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository,
    private val preferences: LocalPreferences,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvent>(replay = 0)
    val events = _events.asSharedFlow()

    private var isLoggingOut = false
    private var loadHomeJob: Job? = null

    init {
        observeSession()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {

            HomeIntent.SearchClicked -> {
                sendEvent(HomeEvent.NavigateToSearch)
            }

            HomeIntent.LogoutClicked -> {
                logout()
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

    private fun observeSession() {
        viewModelScope.launch {
            preferences.activeUserId.collectLatest {
                loadHomeJob?.cancel()
                _uiState.value = HomeState(
                    isLoading = true,
                )

                loadHomeData()
            }
        }
    }

    private fun loadHomeData() {
        loadHomeJob = viewModelScope.launch {
            try {
                val homeData = repository.getHomeData()
                _uiState.update {
                    it.copy(
                        userName = homeData.name.orEmpty(),
                        city = homeData.city.orEmpty(),
                        trendingMovies = homeData.trendingMovies,
                        recommendedMovies = homeData.recommendedMovies,
                        isLoading = false,
                        errorMsg = null,
                    )
                }

            } catch (exception: CancellationException) {
                throw exception

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

    private fun logout() {
        if (isLoggingOut) {
            return
        }

        isLoggingOut = true

        viewModelScope.launch {
            try {
                preferences.logout()

                _events.emit(
                    HomeEvent.NavigateToSignIn
                )

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {

                _events.emit(
                    HomeEvent.LogoutFailed
                )
            } finally {
                isLoggingOut = false
            }
        }
    }

    private fun sendEvent(event: HomeEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}