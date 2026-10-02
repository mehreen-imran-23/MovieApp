package com.example.movieapp.di
import com.example.movieapp.BuildConfig
import com.example.movieapp.data.remote.Api
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single<OkHttpClient> {
        OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header(
                        "Authorization",
                        "Bearer ${BuildConfig.TMDB_READ_ACCESS_TOKEN}",
                    )
                    .header("Accept", "application/json").build()
                chain.proceed(request)
            }
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<Api> {
        get<Retrofit>().create(Api::class.java)
    }
}