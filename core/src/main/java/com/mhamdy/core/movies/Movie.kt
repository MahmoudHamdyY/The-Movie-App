package com.mhamdy.core.movies

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("revenue") val revenue: String,
    @SerializedName("status") val status: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("watch_listed") var watchListed: Boolean?
) : Serializable {
    fun posterUrl() = "https://image.tmdb.org/t/p/w342${posterPath}"
    fun backdropUrl() = "https://image.tmdb.org/t/p/w780${backdropPath}"
}

data class MoviesPage(
    @SerializedName("page") val page: Int,
    @SerializedName("results") var movies: List<Movie>,
    @SerializedName("total_pages") val totalPages: Int
) : Serializable

data class MoviesCard(
    val movie: Movie? = null,
    val header: String? = null,
    val currentPage: Int? = null,
    val canLoadMore: Boolean? = null
) : Serializable
