package com.example.movieapp.feature.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.repository.HomeRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val preferences: LocalPreferences,
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _event = Channel<SplashEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            try {
                val userId = preferences.activeUserId.first()
                val isGuest = preferences.isGuest.first()
                val onboardingCompleted =
                    preferences.onboardingCompleted.first()

                val destination = if (userId != null || isGuest) {
                    SplashEvent.NavigateToHome
                } else if (onboardingCompleted) {
                    SplashEvent.NavigateToSignIn
                } else {
                    SplashEvent.Navigate
                }

                val preloadJob = launch {
                    if (destination == SplashEvent.NavigateToHome) {
                        preloadHome()
                    }
                }

                delay(800L)
                preloadJob.join()

                _event.send(destination)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(
                    "SplashViewModel",
                    "Could not read saved session",
                    exception,
                )
            }
        }
    }

    private suspend fun preloadHome() {
        try {
            homeRepository.preloadHomeData()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Log.e(
                "SplashViewModel",
                "Could not preload Home",
                exception,
            )
        }
    }
}