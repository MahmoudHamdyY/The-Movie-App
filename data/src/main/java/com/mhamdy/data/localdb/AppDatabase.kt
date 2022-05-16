package com.mhamdy.data.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mhamdy.data.domain
import com.mhamdy.data.localdb.daos.MovieDao
import com.mhamdy.data.localdb.entities.MovieDB

@Database(entities = [MovieDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

object DatabaseBuilder {

    private var INSTANCE: AppDatabase? = null

    fun getInstance(): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = buildRoomDB(domain)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "the_movie_app.db"
        ).build()
}