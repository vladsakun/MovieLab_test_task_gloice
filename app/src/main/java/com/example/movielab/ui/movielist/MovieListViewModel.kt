package com.example.movielab.ui.movielist

import androidx.lifecycle.ViewModel
import com.example.movielab.data.repository.MovieRepository
import com.example.movielab.internal.lazyDeferred

class MovieListViewModel (
    private val movieRepository: MovieRepository
) : ViewModel() {

    //Get movie list from db
    val movieList by lazyDeferred {
        movieRepository.getMovieList()
    }
}