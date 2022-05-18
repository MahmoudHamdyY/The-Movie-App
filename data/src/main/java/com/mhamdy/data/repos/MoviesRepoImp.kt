package com.mhamdy.data.repos

import com.mhamdy.core.CoreIntegrationDsl
import com.mhamdy.core.movies.Movie
import com.mhamdy.core.movies.MoviesPage
import com.mhamdy.core.movies.MoviesRepo
import com.mhamdy.data.localdb.AppDatabase
import com.mhamdy.data.localdb.DatabaseBuilder
import com.mhamdy.data.localdb.entities.MovieDB
import com.mhamdy.data.network.Api
import com.mhamdy.data.network.getApiService

class MoviesRepoImp @CoreIntegrationDsl constructor(
    private val api: Api = getApiService(),
    private val db: AppDatabase = DatabaseBuilder.getInstance()
) : MoviesRepo {

    override suspend fun getMovie(id: Int): Movie {
        return runCatching {
            api.getMovie(id)
        }.getOrElse {
            throw Exception("Something Went Wrong", it)
        }
    }

    override suspend fun getPopularMovies(page: Int): MoviesPage {
        return runCatching {
            api.getPopularMovies(page)
        }.getOrElse {
            throw Exception("Something Went Wrong", it)
        }
    }

    override suspend fun searchMovies(query: String, page: Int): MoviesPage {
        return runCatching {
            api.searchMovies(query, page)
        }.getOrElse {
            throw Exception("Something Went Wrong", it)
        }
    }

    override suspend fun getSimilarMovies(id: Int): MoviesPage {
        return runCatching {
            api.getSimilarMovies(id)
        }.getOrElse {
            throw Exception("Something Went Wrong", it)
        }
    }

    override suspend fun getWatchListedMoviesIds(): List<Int> {
        return runCatching {
            db.movieDao().getAllMoviesIds()
        }.getOrElse {
            throw Exception("Something Went Wrong", it)
        }
    }

    override suspend fun addMovieToWatchList(movie: Movie) {
        runCatching {
            db.movieDao().insert(MovieDB(movie))
        }.getOrElse {
            throw Exception("Something Went Wrong", it)
        }
    }

    override suspend fun removeMovieFromWatchList(movie: Movie) {
        runCatching {
            db.movieDao().delete(MovieDB(movie))
        }.getOrElse {
            throw Exception("Something Went Wrong", it)
        }
    }
}