package com.example.movieapp.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.feature.Onboarding1.Onboarding1Intent
import com.example.movieapp.feature.Onboarding1.Onboarding1State
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Onboarding1ViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(Onboarding1State())
    val uiState: StateFlow<Onboarding1State> =
        _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Onboarding1Event>(
        replay = 0,
    )
    val events: SharedFlow<Onboarding1Event> =
        _events.asSharedFlow()

    fun onIntent(intent: Onboarding1Intent) {
        when (intent) {
            Onboarding1Intent.Next -> {
                sendEvent(Onboarding1Event.NavigateToGenres)
            }

            Onboarding1Intent.Skip -> {
                sendEvent(Onboarding1Event.NavigateToSignIn)
            }
        }
    }

    private fun sendEvent(event: Onboarding1Event) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}