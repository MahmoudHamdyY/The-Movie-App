package com.mhamdy.core.movies

import com.mhamdy.core.extentions.getYear
import com.mhamdy.core.moviesRepoImp

suspend fun getMovie(
    id: Int, moviesRepo: MoviesRepo = moviesRepoImp
): Movie {
    val watchListedIds = moviesRepo.getWatchListedMoviesIds()
    return moviesRepo.getMovie(id).apply {
        watchListed = watchListedIds.contains(id)
    }
}

suspend fun getPopularMovies(
    page: Int = 1, moviesRepo: MoviesRepo = moviesRepoImp
): MoviesPage =
    moviesRepo.getPopularMovies(page)

suspend fun searchMovies(
    query: String,
    page: Int = 1,
    moviesRepo: MoviesRepo = moviesRepoImp
): MoviesPage =
    moviesRepo.searchMovies(query, page)

suspend fun getSimilarMovies(
    id: Int,
    moviesRepo: MoviesRepo = moviesRepoImp
): List<Movie> =
    moviesRepo.getSimilarMovies(id).apply {
        movies = this.movies.take(5)
    }.movies

suspend fun addMovieToWatchList(
    movie: Movie,
    moviesRepo: MoviesRepo = moviesRepoImp
) =
    moviesRepo.addMovieToWatchList(movie.apply { watchListed = true })

suspend fun removeMovieFromWatchList(
    movie: Movie,
    moviesRepo: MoviesRepo = moviesRepoImp
) =
    moviesRepo.removeMovieFromWatchList(movie)

suspend fun getWatchListedMoviesIds(moviesRepo: MoviesRepo = moviesRepoImp) =
    moviesRepo.getWatchListedMoviesIds()

suspend fun MutableList<MoviesCard>.append(
    moviesPage: MoviesPage,
    moviesRepo: MoviesRepo = moviesRepoImp
) {
    val watchListedIds = moviesRepo.getWatchListedMoviesIds()
    var currentYear: Int = if (!isEmpty())
        last { it.movie != null }.movie!!.releaseDate.getYear()
    else
        0
    moviesPage.movies.forEach { movie ->
        if (movie.releaseDate.isNotEmpty() && movie.releaseDate.getYear() != currentYear) {
            add(MoviesCard(header = movie.releaseDate.getYear().toString()))
            currentYear = movie.releaseDate.getYear()
        }
        add(
            MoviesCard(
                movie = movie.apply { watchListed = watchListedIds.contains(movie.id) },
                currentPage = moviesPage.page,
                canLoadMore = moviesPage.page < moviesPage.totalPages
            )
        )
    }
}