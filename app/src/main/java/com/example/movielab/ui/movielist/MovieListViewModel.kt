package com.example.movielab.ui.movielist

import androidx.lifecycle.ViewModel
import com.example.movielab.data.repository.MovieRepository
import com.example.movielab.internal.lazyDeferred

class MovieListViewModel (
    private val movieRepository: MovieRepository
) : ViewModel() {

    val movieList by lazyDeferred {
        movieRepository.getMovieList()
    }
}