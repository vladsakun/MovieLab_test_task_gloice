package com.example.movielab.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movielab.data.exceptions.NoConnectivityException
import com.example.movielab.data.response.MovieListResponse

class MovieNetworkDataSourceImpl(
    private val sunApiService: MovieApiService
) : MovieNetworkDataSource {

    private val _downloadedMovieList = MutableLiveData<MovieListResponse>()
    override val downloadedMovieList: LiveData<MovieListResponse>
        get() = _downloadedMovieList

    override suspend fun fetchMovieList() {
        try {
            val fetchedSunInfo = sunApiService.getMoviesList().await()
            _downloadedMovieList.postValue(fetchedSunInfo)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}