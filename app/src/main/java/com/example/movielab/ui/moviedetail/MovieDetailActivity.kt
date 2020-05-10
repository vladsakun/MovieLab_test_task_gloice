package com.example.movielab.ui.moviedetail

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
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

        val titleDate = movie.title + " (" + movie.release_date.substring(0,4) + ")"
        val spannableStringBuilder = SpannableStringBuilder(titleDate)

        val gray = ForegroundColorSpan(getColor(R.color.gray))

        spannableStringBuilder.setSpan(gray, movie.title.length, titleDate.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        mHeaderTitle.text = spannableStringBuilder

        // Set vote average TextView to the item's vote average
        mHeaderVoteAverage.text = movie.vote_average.toString()

        // Set overview TextView to the item's overview
        overview_text.text = movie.overview

        // Set image ImageView to the item's image
        loadThumbnail()
    }

    private fun loadThumbnail() {

        val bm = BitmapFactory.decodeByteArray(
            movie.image,
            0,
            movie.image.size
        )

        imageview_header.setImageBitmap(bm)
    }

    companion object {
        // View name of the header image. Used for activity scene transitions
        const val VIEW_NAME_HEADER_IMAGE = "detail:header:image"

        // View name of the header title. Used for activity scene transitions
        const val VIEW_NAME_HEADER_TITLE = "detail:header:title"

        // View name of the vote average. Used for activity scene transitions
        const val VIEW_NAME_HEADER_VOTE_AVERAGE = "detail:header:vote_average"

    }

}
