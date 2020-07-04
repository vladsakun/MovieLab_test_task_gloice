package com.example.movielab.ui.movielist

import androidx.lifecycle.ViewModel
import com.example.movielab.data.repository.MovieRepository
import com.example.movielab.internal.lazyDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Callback

class MovieListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    //Get movie list from db
    val movieList by lazyDeferred {
        movieRepository.getMovieList()
    }

    //Get searched movies list from api
    val searchedMovies by lazyDeferred {
        movieRepository.getSearchedMovies()
    }

    fun searchMovie(query: String) {
        GlobalScope.launch {
            movieRepository.searchMovies(query)
        }
    }
}