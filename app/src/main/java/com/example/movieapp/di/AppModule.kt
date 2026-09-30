package com.example.movieapp.di

import com.example.movieapp.BuildConfig
import com.example.movieapp.data.local.GenrePreferences
import com.example.movieapp.data.remote.Api
import com.example.movieapp.data.remote.NetworkOnboarding
import com.example.movieapp.data.repository.OnboardingRepository
import com.example.movieapp.feature.auth.AuthEnum
import com.example.movieapp.feature.auth.AuthViewModel
import com.example.movieapp.feature.onboarding.OnboardingViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<OkHttpClient> {
        NetworkOnboarding.createHttpClient(
            token = BuildConfig.TMDB_READ_ACCESS_TOKEN,
        )
    }

    single<Api> {
        NetworkOnboarding.createApi(
            client = get(),
        )
    }

    single {
        OnboardingRepository(
            api = get(),
        )
    }

    viewModel {
        OnboardingViewModel(
            repository = get(),
            preferences = get(),
        )
    }

    viewModel { (mode: AuthEnum) ->
        AuthViewModel(mode = mode)
    }

    single {
        GenrePreferences(context = androidContext())
    }
}