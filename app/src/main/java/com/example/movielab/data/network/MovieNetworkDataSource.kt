package com.example.movielab.data.network

import androidx.lifecycle.LiveData
import com.example.movielab.data.response.MovieListResponse

interface MovieNetworkDataSource {
    val downloadedMovieList: LiveData<MovieListResponse>

    suspend fun fetchMovieList()
}