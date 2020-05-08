package com.example.movielab.data.repository

import androidx.lifecycle.LiveData
import com.example.movielab.data.db.entity.MovieEntity

interface MovieRepository {

    suspend fun getMovieList():LiveData<out List<MovieEntity>>
}