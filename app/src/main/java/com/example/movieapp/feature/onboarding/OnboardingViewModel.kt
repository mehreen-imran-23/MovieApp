package com.example.movieapp.feature.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.repository.OnboardingRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class OnboardingViewModel(
    private val repository: OnboardingRepository,
    private val preferences: LocalPreferences,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingState())
    val uiState: StateFlow<OnboardingState> = _uiState.asStateFlow()
    private var isSavingGenres = false

    private val _events = MutableSharedFlow<Onboarding1Event>(
        replay = 0,
    )
    val events: SharedFlow<Onboarding1Event> = _events.asSharedFlow()

    init {
        loadOnboarding()
    }

    private fun loadOnboarding() {
        viewModelScope.launch {

            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    errorMsg = null,
                )
            }

            try {
                val savedGenreIds = preferences.selectedGenreIds.first()

                _uiState.update { state ->
                    state.copy(selectedGenre = savedGenreIds)
                }
                val urls = repository.getPosterUrls()
                val genres = repository.getGenres()

                val genre = genres.map { genre ->
                    OnboardingGenre(
                        id = genre.id,
                        name = genre.name,
                    )
                }

                _uiState.update { state ->
                    state.copy(
                        posterUrls = urls,
                        genres = genre,
                        isLoading = false,
                        errorMsg = null,
                    )
                }

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {

                val error = when (exception) {
                    is HttpException ->
                        "Loading failed: HTTP ${exception.code()}"

                    else ->
                        "Loading failed: ${exception.message ?: "Unknown error"}"
                }

                Log.e(
                    "OnboardingViewModel",
                    error,
                    exception,
                )

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMsg = error,
                    )
                }
            }
        }
    }

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            OnboardingIntent.Skip -> {
                navigateToSignIn()
            }

            OnboardingIntent.Next -> {
                navigateToSignIn()
            }

            is OnboardingIntent.GenreClicked -> {
                toggleGenre(intent.genreId)
            }
        }
    }

    private fun toggleGenre(genreId: Int) {
        _uiState.update { state ->

            val selectedGenres = state.selectedGenre

            state.copy(
                selectedGenre = if (genreId in selectedGenres) {
                    selectedGenres - genreId
                } else {
                    selectedGenres + genreId
                },
            )
        }
    }

    private fun navigateToSignIn() {
        if (isSavingGenres) {
            return
        }
        isSavingGenres = true

        viewModelScope.launch {
            try {
                val selectedIds =
                    if (_uiState.value.isLoading) {
                        preferences.selectedGenreIds.first()
                    } else {
                        _uiState.value.selectedGenre
                    }

                preferences.saveSelectedGenres(selectedIds)

                _events.emit(Onboarding1Event.NavigateToSignIn)
            }
            catch (exception: CancellationException)
            {
                throw exception
            }
            catch (exception: Exception)
            {
                Log.e(
                    "OnboardingViewModel",
                    "Could not save selected genres",
                    exception,
                )

                _uiState.update { state ->
                    state.copy(
                        errorMsg = "Could not save your selection. Please try again.",
                    )
                }
            } finally
            {
                isSavingGenres = false
            }
        }
    }
}