package com.example.movielab.data.repository

import androidx.lifecycle.LiveData
import com.example.movielab.data.db.dao.MovieDao
import com.example.movielab.data.db.entity.MovieEntity
import com.example.movielab.data.network.MovieNetworkDataSource
import com.example.movielab.data.response.MovieListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class MovieRepositoryImpl(

    private val movieDao: MovieDao,
    private val movieNetworkDataSource: MovieNetworkDataSource

) : MovieRepository {

    init {
        movieNetworkDataSource.apply {
            downloadedMovieList.observeForever { newMovieResponse ->
                persistFetchedMovies(newMovieResponse)
            }
        }
    }

    private fun persistFetchedMovies(newMovieResponse: MovieListResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            movieDao.upsert(convertMovieListResponseToListOfEntities(newMovieResponse))
        }
    }

    private fun convertMovieListResponseToListOfEntities(newMovieResponse: MovieListResponse): List<MovieEntity> {
        val movieEntityList: ArrayList<MovieEntity> = arrayListOf()

        for (movie in newMovieResponse.results) {

            movieEntityList.add(
                MovieEntity(
                    movie.id, movie.popularity, movie.adult, movie.original_title,
                    movie.title, movie.overview, movie.release_date, movie.poster_path,
                    movie.vote_average
                )
            )

        }
        return movieEntityList
    }

    override suspend fun getMovieList(): LiveData<List<MovieEntity>> {
        return withContext(Dispatchers.IO) {
            initMovieData()
            return@withContext movieDao.getListOfMovies()
        }
    }

    private suspend fun initMovieData() {
        fetchMovies()
    }

    private suspend fun fetchMovies() {
        movieNetworkDataSource.fetchMovieList()
    }

}