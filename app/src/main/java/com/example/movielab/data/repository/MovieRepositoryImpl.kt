package com.example.movielab.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat.getDrawable
import androidx.lifecycle.LiveData
import com.example.movielab.R
import com.example.movielab.data.db.dao.MovieDao
import com.example.movielab.data.db.entity.MovieEntity
import com.example.movielab.data.network.MovieApiService.Companion.IMAGE_BASE_URL
import com.example.movielab.data.network.MovieNetworkDataSource
import com.example.movielab.data.response.MovieListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


class MovieRepositoryImpl(
    private var context: Context,
    private val movieDao: MovieDao,
    private val movieNetworkDataSource: MovieNetworkDataSource

) : MovieRepository {

    init {
        context = context.applicationContext

        movieNetworkDataSource.apply {

            //Set observer on fetched popular movies
            downloadedMovieList.observeForever { newMovieResponse ->
                persistFetchedMovies(newMovieResponse)
            }
        }
    }

    //Add new movies to local db
    private fun persistFetchedMovies(newMovieResponse: MovieListResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            movieDao.upsert(convertMovieListResponseToListOfEntities(newMovieResponse))
        }
    }

    //Convert movies response to list of movie entities for db
    private fun convertMovieListResponseToListOfEntities(newMovieResponse: MovieListResponse): List<MovieEntity> {
        val movieEntityList: ArrayList<MovieEntity> = arrayListOf()

        for (movie in newMovieResponse.results) {

            movieEntityList.add(
                MovieEntity(
                    movie.id, movie.popularity, movie.adult, movie.original_title,
                    movie.title, movie.overview, movie.release_date, movie.poster_path,
                    movie.vote_average, getByteArrayImage(IMAGE_BASE_URL + movie.poster_path)
                )
            )

        }
        return movieEntityList
    }

    //Select all movies from db and return them
    override suspend fun getMovieList(): LiveData<List<MovieEntity>> {
        return withContext(Dispatchers.IO) {
            initMovieData()
            return@withContext movieDao.getListOfMovies()
        }
    }

    private suspend fun initMovieData() {
        fetchMovies()
    }

    //Fetch movies from api
    private suspend fun fetchMovies() {
        movieNetworkDataSource.fetchMovieList()
    }

    //Download image from url and convert it to byte array
    private fun getByteArrayImage(url: String): ByteArray {
        try {
            val imageUrl = URL(url)
            val ucon: URLConnection = imageUrl.openConnection()
            val `is`: InputStream = ucon.getInputStream()
            val bis = BufferedInputStream(`is`)
            val buffer = ByteArrayOutputStream()
            //We create an array of bytes
            val data = ByteArray(50)
            var current = 0

            while (bis.read(data, 0, data.size).also { current = it } != -1) {
                buffer.write(data, 0, current)
            }
            return buffer.toByteArray()
        } catch (e: Exception) {
            Log.d("ImageManager", "Error: $e")
        }

        // If could not download image from url return default poster
        val d: Drawable? = getDrawable(context, R.drawable.default_movie)
        val bitmap = (d as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        return stream.toByteArray()
    }

}