package com.example.movieapp.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.repository.SearchRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchRepository,
    private val preferences: LocalPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<SearchEvent>(replay = 0)
    val events = _events.asSharedFlow()

    private var searchJob: Job? = null
    private var loadMoreJob: Job? = null
    private var currPage = 0
    private var movieClickJob: Job? = null

    init {
        observeRecentSearches()
    }

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> {
                searchMovies(
                    query = intent.query,
                    debounce = true,
                )
            }

            SearchIntent.LoadMore -> {
                loadMore()
            }

            SearchIntent.SearchClicked -> {
                searchMovies(_uiState.value.query)
            }

            is SearchIntent.RecentSearchClicked -> {
                searchMovies(intent.query)
            }

            is SearchIntent.MovieClicked -> {
                openMovie(intent.movieId)
            }

            SearchIntent.BackClicked -> {
                sendEvent(SearchEvent.NavigateBack)
            }
        }
    }

    private fun observeRecentSearches() {
        viewModelScope.launch {
            try {
                preferences.activeUserId.collectLatest { userId ->
                    _uiState.update { state ->
                        state.copy(recentMovies = emptyList())
                    }

                    repository.observeSearches(userId).collect { movies ->
                        _uiState.update { state ->
                            state.copy(recentMovies = movies)
                        }
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { state ->
                    state.copy(errorMsg = R.string.search_history)
                }
            }
        }
    }

    private fun searchMovies(
        query: String,
        debounce: Boolean = false,
    ) {
        searchJob?.cancel()
        loadMoreJob?.cancel()
        currPage = 0

        val searchQuery = query.trim()

        _uiState.update { state ->
            state.copy(
                query = query,
                movies = emptyList(),
                isLoading = searchQuery.isNotEmpty(),
                isLoadingMore = false,
                hasSearched = false,
                NextPage = false,
                errorMsg = null,
                loadMoreError = null,
            )
        }

        if (searchQuery.isEmpty()) {
            return
        }

        searchJob = viewModelScope.launch {
            try {
                if (debounce) {
                    delay(300)
                }

                val result = repository.searchMovies(
                    query = searchQuery,
                    page = 1,
                )

                currentCoroutineContext().ensureActive()
                currPage = result.page

                _uiState.update { state ->
                    state.copy(
                        movies = result.movies.distinctBy { it.id },
                        isLoading = false,
                        hasSearched = true,
                        NextPage = result.page < result.totalPages,
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        hasSearched = true,
                        errorMsg = R.string.search_failed,
                    )
                }
            }
        }
    }

    private fun loadMore() {
        val state = _uiState.value

        if (
            state.query.isBlank() ||
            state.isLoading ||
            state.isLoadingMore ||
            !state.NextPage
        ) {
            return
        }

        val query = state.query.trim()
        val nextPage = currPage + 1

        _uiState.update {
            it.copy(
                isLoadingMore = true,
                loadMoreError = null,
            )
        }

        loadMoreJob = viewModelScope.launch {
            try {
//                delay(10000L)

                val result = repository.searchMovies(
                    query = query,
                    page = nextPage,
                )

                currentCoroutineContext().ensureActive()
                currPage = result.page

                _uiState.update { currentState ->
                    currentState.copy(
                        movies = (currentState.movies + result.movies)
                            .distinctBy { it.id },
                        isLoadingMore = false,
                        NextPage = result.page < result.totalPages,
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        loadMoreError = R.string.search_failed,
                    )
                }
            }
        }
    }

    private fun openMovie(movieId: Int) {
        if (movieClickJob?.isActive == true) return

        val state = _uiState.value

        val movie = state.movies.find { it.id == movieId }
            ?: state.recentMovies.find { it.id == movieId }
            ?: return

        movieClickJob = viewModelScope.launch {
            try {
                val userId = preferences.activeUserId.first()

                repository.saveSearch(
                    userId = userId,
                    movies = listOf(movie),
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMsg = R.string.search_save_failed,
                    )
                }
            }

            _events.emit(
                SearchEvent.NavigateToMovieDetails(
                    movieId = movieId,
                )
            )
        }
    }

    private fun sendEvent(event: SearchEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}