package com.mhamdy.data.localdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieDB(
    @PrimaryKey
    val id: Int,
    val title: String,
    val releaseDate: String,
    val revenue: String,
    val status: String,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val tagline: String?,
    var watchListed: Boolean?
)