package com.example.movielab.ui.movielist

import ItemOffsetDecoration
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielab.R
import com.example.movielab.adapters.MovieListAdapter
import com.example.movielab.data.db.entity.MovieEntity
import com.example.movielab.ui.ScopedActivity
import kotlinx.android.synthetic.main.custom_progress_bar.*
import kotlinx.android.synthetic.main.movie_list_activity.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class MovieListActivity : ScopedActivity(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: MovieListViewModelFactory by instance()
    private lateinit var viewModel: MovieListViewModel

    private lateinit var adapter: MovieListAdapter
    private var movieEntityList = listOf<MovieEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_list_activity)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieListViewModel::class.java)

        adapter = MovieListAdapter(this, this, movieEntityList)

        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this, 2)
        val itemDecoration = ItemOffsetDecoration(this, R.dimen.item_offset)

        recyclerview.addItemDecoration(itemDecoration)

        bindUI()

    }

    private fun bindUI() = launch {
        val movieList = viewModel.movieList.await()

        movieList.observe(this@MovieListActivity, Observer {
            if (it == null) return@Observer

            updateMovieAdapter(it)
            progressBar.visibility = View.GONE
        })
    }

    private fun updateMovieAdapter(it: List<MovieEntity>) {
        adapter.moviesArrayList = it
        adapter.notifyDataSetChanged()
    }
}
