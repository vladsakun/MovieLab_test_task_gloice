package com.example.movielab.ui.moviedetail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.movielab.MOVIE_MESSAGE
import com.example.movielab.R
import com.example.movielab.data.db.entity.MovieEntity
import com.example.movielab.data.network.MovieApiService.Companion.IMAGE_BASE_URL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var mHeaderTitle: TextView
    private lateinit var mHeaderVoteAverage: TextView
    private lateinit var mHeaderImageView: ImageView
    private lateinit var movie: MovieEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        mHeaderImageView = findViewById(R.id.imageview_header)
        mHeaderTitle = findViewById(R.id.textview_title)
        mHeaderVoteAverage = findViewById(R.id.rating)

        ViewCompat.setTransitionName(imageview_header, VIEW_NAME_HEADER_IMAGE)
        ViewCompat.setTransitionName(textview_title, VIEW_NAME_HEADER_TITLE)
        ViewCompat.setTransitionName(mHeaderVoteAverage, VIEW_NAME_HEADER_VOTE_AVERAGE)

        movie = intent.getSerializableExtra(MOVIE_MESSAGE) as MovieEntity

        loadItem()
    }

    private fun loadItem() {
        // Set the title TextView to the item's name
        mHeaderTitle.text = getString(R.string.movie_name, movie.title, movie.release_date)
        mHeaderVoteAverage.text = movie.vote_average.toString()
        overview_text.text = movie.overview
        loadThumbnail()
    }

    private fun loadThumbnail() {
        Picasso.get().load(IMAGE_BASE_URL + movie.poster_path)
            .into(mHeaderImageView)
    }

    companion object {
        // View name of the header image. Used for activity scene transitions
        const val VIEW_NAME_HEADER_IMAGE = "detail:header:image"

        // View name of the header title. Used for activity scene transitions
        const val VIEW_NAME_HEADER_TITLE = "detail:header:title"

        // View name of the header title. Used for activity scene transitions
        const val VIEW_NAME_HEADER_VOTE_AVERAGE = "detail:header:vote_average"
    }

}
