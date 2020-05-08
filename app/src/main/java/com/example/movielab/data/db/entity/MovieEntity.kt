package com.example.movielab.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "movie_db")
class MovieEntity(

    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val popularity: Double,
    val adult: Boolean,
    val original_title: String,
    val title: String,
    val overview: String,
    val release_date: String,
    val poster_path: String,
    val vote_average: Double
) : Serializable


