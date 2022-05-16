package com.mhamdy.data.localdb.daos

import androidx.room.*
import com.mhamdy.data.localdb.entities.MovieDB

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    abstract fun getAllMovies(): List<MovieDB>

    @Query("SELECT id FROM movies")
    abstract fun getAllMoviesIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: MovieDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: List<MovieDB>)

    @Delete
    abstract fun delete(movie: MovieDB)
}