package com.mhamdy.data.localdb.daos

import androidx.room.*
import com.mhamdy.data.localdb.entities.MovieDB

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    abstract suspend fun getAllMovies(): List<MovieDB>

    @Query("SELECT id FROM movies")
    abstract suspend fun getAllMoviesIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(movie: MovieDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(movie: List<MovieDB>)

    @Delete
    abstract suspend fun delete(movie: MovieDB)
}