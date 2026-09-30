package com.example.movieapp.di

import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.movieapp.BuildConfig
import com.example.movieapp.data.local.AppDatabase
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.local.PassHash
import com.example.movieapp.data.remote.Api
import com.example.movieapp.data.remote.NetworkOnboarding
import com.example.movieapp.data.repository.AuthRepository
import com.example.movieapp.data.repository.OnboardingRepository
import com.example.movieapp.feature.auth.AuthEnum
import com.example.movieapp.feature.auth.AuthViewModel
import com.example.movieapp.feature.onboarding.OnboardingViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        NetworkOnboarding()
    }

    single<OkHttpClient> {
        get<NetworkOnboarding>().createHttpClient(
            token = BuildConfig.TMDB_READ_ACCESS_TOKEN,
        )
    }

    single<Api> {
        get<NetworkOnboarding>().createApi(
            client = get(),
        )
    }

    single<AppDatabase> {
        Room.databaseBuilder<AppDatabase>(
            context = androidContext(),
            name = "movie_app.db",
        )
            .setDriver(AndroidSQLiteDriver())
            .build()
    }

    single {
        get<AppDatabase>().userDao()
    }

    single {
        PassHash()
    }

    single {
        AuthRepository(
            userDao = get(),
            passwHash = get(),
        )
    }

    single {
        OnboardingRepository(
            api = get(),
        )
    }

    single {
        LocalPreferences(
            context = androidContext(),
        )
    }

    viewModel {
        OnboardingViewModel(
            repository = get(),
            preferences = get(),
        )
    }

    viewModel { (mode: AuthEnum) ->
        AuthViewModel(mode = mode, repository = get(), preferences = get(),
        )
    }
}