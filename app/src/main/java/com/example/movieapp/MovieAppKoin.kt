package com.example.movieapp

import android.app.Application
import com.example.movieapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieAppKoin : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@MovieAppKoin)

            modules(appModule)
        }
    }
}