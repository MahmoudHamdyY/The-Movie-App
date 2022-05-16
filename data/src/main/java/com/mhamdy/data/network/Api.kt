package com.mhamdy.data.network

import com.mhamdy.core.credits.Credits
import com.mhamdy.core.movies.Movie
import com.mhamdy.core.movies.MoviesPage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int = 1): MoviesPage

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MoviesPage

    @GET("movie/{id}")
    suspend fun getMovie(@Path("id") movieId: Int): Movie

    @GET("movie/{id}/similar")
    suspend fun getSimilarMovies(
        @Path("id") movieId: Int
    ): MoviesPage

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(@Path("id") movieId: Int): Credits

    @GET("configuration")
    suspend fun getConfiguration()
}