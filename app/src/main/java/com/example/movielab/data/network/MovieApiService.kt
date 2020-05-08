package com.example.movielab.data.network

import com.example.movielab.data.response.MovieListResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.*

interface MovieApiService {

    @GET("popular")
    fun getMoviesList(
    ): Deferred<MovieListResponse>

    companion object {

        const val BASE_URL = "https://api.themoviedb.org/3/movie/"
        const val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/"
        const val API_KEY = "78668797853c3b31320011e0e411b0a6"
        const val DEFAULT_PAGE_COUNT = "1"

        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): MovieApiService {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .addQueryParameter("language", Locale.getDefault().toLanguageTag())
                    .addQueryParameter("page", DEFAULT_PAGE_COUNT)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieApiService::class.java)
        }
    }
}