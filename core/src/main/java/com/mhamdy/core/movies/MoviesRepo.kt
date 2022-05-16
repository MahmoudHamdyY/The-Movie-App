package com.mhamdy.core.movies

interface MoviesRepo {
    suspend fun getMovie(id: Int): Movie
    suspend fun getPopularMovies(page: Int = 1): MoviesPage
    suspend fun searchMovies(query: String, page: Int = 1): MoviesPage
    suspend fun getSimilarMovies(id: Int): MoviesPage
    suspend fun getWatchListedMoviesIds(): List<Int>
}