package com.example.movielab

import android.app.Application
import com.example.movielab.data.db.MovieLabDatabase
import com.example.movielab.data.network.*
import com.example.movielab.data.repository.MovieRepository
import com.example.movielab.data.repository.MovieRepositoryImpl
import com.example.movielab.ui.movielist.MovieListViewModelFactory
import okhttp3.internal.connection.ConnectInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MovieLabApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MovieLabApplication))

        bind() from singleton { MovieLabDatabase(instance()) }

        //Dao
        bind() from singleton { instance<MovieLabDatabase>().movieDao() }

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }

        //DataSource
        bind<MovieNetworkDataSource>() with singleton { MovieNetworkDataSourceImpl(instance()) }

        //Api service
        bind() from singleton { MovieApiService(instance()) }

        //Repository
        bind<MovieRepository>() with singleton { MovieRepositoryImpl(instance(), instance()) }

        //ViewModelFactories
        bind() from provider {  MovieListViewModelFactory(instance())}
    }
}