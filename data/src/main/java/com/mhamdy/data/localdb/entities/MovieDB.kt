package com.mhamdy.data.localdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mhamdy.core.movies.Movie

@Entity(tableName = "movies")
data class MovieDB(
    @PrimaryKey
    val id: Int,
    val title: String,
    val releaseDate: String,
    val revenue: Int,
    val status: String,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val tagline: String?,
    var watchListed: Boolean?
) {
    constructor(movie: Movie) : this(
        movie.id,
        movie.title,
        movie.releaseDate,
        movie.revenue,
        movie.status,
        movie.overview,
        movie.posterPath,
        movie.backdropPath,
        movie.tagline,
        movie.watchListed
    )
}