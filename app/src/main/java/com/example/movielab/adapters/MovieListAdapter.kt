package com.example.movielab.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.example.movielab.MOVIE_MESSAGE
import com.example.movielab.R
import com.example.movielab.data.db.entity.MovieEntity
import com.example.movielab.ui.moviedetail.MovieDetailActivity


class MovieListAdapter(
    private val context: Context,
    private val activity: AppCompatActivity,
    var moviesArrayList: List<MovieEntity>

) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    inner class MovieListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Init views
        var moviePoster: ImageView = itemView.findViewById(R.id.movie_poster)
        var movieName: TextView = itemView.findViewById(R.id.movie_name)
        var movieRating: TextView = itemView.findViewById(R.id.rating)
        var movieReleaseDate: TextView = itemView.findViewById(R.id.movie_release_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val view = inflater.inflate(R.layout.movie_item, parent, false)
        return MovieListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moviesArrayList.size
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {

        //Set text values to view of item's values
        holder.movieName.text = moviesArrayList[position].title
        holder.movieReleaseDate.text = moviesArrayList[position].release_date
        holder.movieRating.text = moviesArrayList[position].vote_average.toString()

        //convert byte array to bitmap
        val bm = BitmapFactory.decodeByteArray(
            moviesArrayList[position].image,
            0,
            moviesArrayList[position].image.size
        )

        //Set bitmap image on movie poster
        holder.moviePoster.setImageBitmap(bm)

        //On movie click switch on Detail Activity
        holder.itemView.setOnClickListener {

            //Pairs of views
            val p1 = Pair.create<View, String>(
                holder.moviePoster,
                MovieDetailActivity.VIEW_NAME_HEADER_IMAGE
            )
            val p2 = Pair.create<View, String>(
                holder.movieName,
                MovieDetailActivity.VIEW_NAME_HEADER_TITLE
            )

            val p3 = Pair.create<View, String>(
                holder.movieRating,
                MovieDetailActivity.VIEW_NAME_HEADER_VOTE_AVERAGE
            )

            // Construct an Intent
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(MOVIE_MESSAGE, moviesArrayList[position])

            //Set pairs for transition
            val activityOptions: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, p1, p2, p3
                )

            //Start the Activity, providing the activity options as a bundle
            context.startActivity(intent, activityOptions.toBundle())
        }
    }
}