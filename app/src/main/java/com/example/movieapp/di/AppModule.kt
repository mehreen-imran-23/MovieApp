package com.example.movieapp.di

import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.movieapp.BuildConfig
import com.example.movieapp.data.local.AppDatabase
import com.example.movieapp.data.local.LocalPreferences
import com.example.movieapp.data.local.PassHash
import com.example.movieapp.data.local.migration_2_3
import com.example.movieapp.data.local.migration_3_4
import com.example.movieapp.data.local.migration_4_5
import com.example.movieapp.data.local.migration_5_6
import com.example.movieapp.data.mapper.HomeMapper
import com.example.movieapp.data.mapper.MovieDetailsMapper
import com.example.movieapp.data.remote.Api
import com.example.movieapp.data.repository.AuthRepository
import com.example.movieapp.data.repository.HomeRepository
import com.example.movieapp.data.repository.MovieDetailsRepository
import com.example.movieapp.data.repository.OnboardingRepository
import com.example.movieapp.data.repository.ProfileRepository
import com.example.movieapp.data.repository.SearchRepository
import com.example.movieapp.feature.afterSucessProfile.ProfileViewModel
import com.example.movieapp.feature.auth.AuthEnum
import com.example.movieapp.feature.auth.AuthViewModel
import com.example.movieapp.feature.home.HomeViewModel
import com.example.movieapp.feature.moviedetails.MovieDetailsViewModel
import com.example.movieapp.feature.onboarding.OnboardingViewModel
import com.example.movieapp.feature.search.SearchViewModel
import com.example.movieapp.feature.splash.SplashViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single {
        NetworkModule()
    }

    single<OkHttpClient> {
        get<NetworkModule>().createHttpClient(
            token = BuildConfig.TMDB_READ_ACCESS_TOKEN,
        )
    }

    single<Api> {
        get<NetworkModule>().createApi(get())
    }

    single<AppDatabase> {
        Room.databaseBuilder<AppDatabase>(
            context = androidContext(),
            name = "movie_app.db",
        )
            .addMigrations(migration_2_3, migration_3_4, migration_4_5, migration_5_6)
            .setDriver(AndroidSQLiteDriver())
            .build()
    }

    single {
        get<AppDatabase>().userDao()
    }

    single {
        get<AppDatabase>().SearchDao()
    }

    single {
        PassHash()
    }

    single {
        AuthRepository(get(), get())
    }

    single {
        OnboardingRepository(get())
    }

    single {
        LocalPreferences(context = androidContext(), get())
    }

    viewModel {
        OnboardingViewModel(get(), get())
    }

    viewModel { (mode: AuthEnum) ->
        AuthViewModel(mode = mode, get(), get())
    }

    viewModel {
        SplashViewModel(
            get(), get(), get()
        )
    }

    single {
        ProfileRepository(get())
    }

    viewModel {
        ProfileViewModel(get(), get())
    }

    single {
        HomeMapper()
    }

    single {
        HomeRepository(get(), get(), get(), get())
    }

    viewModel {
        HomeViewModel(get(), get())
    }

    single {
        SearchRepository(get(), get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }
    single {
        MovieDetailsMapper()
    }

    single {
        MovieDetailsRepository(get(), get())
    }

    viewModel { parameters ->
        MovieDetailsViewModel(
            get(), movieId = parameters.get<Int>(),
            countryCode = parameters.get<String>(),
        )
    }
}