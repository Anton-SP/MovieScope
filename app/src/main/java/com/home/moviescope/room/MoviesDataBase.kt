package com.home.moviescope.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.home.moviescope.room.Genres.GenresDao
import com.home.moviescope.room.Genres.GenresEntity
import com.home.moviescope.room.Movies.MoviesDao
import com.home.moviescope.room.Movies.MoviesEntity

@Database(
    entities = arrayOf(GenresEntity::class),
    version = 1,
    exportSchema = false
)
abstract class MoviesDataBase : RoomDatabase() {
    abstract fun genresDao(): GenresDao
 //   abstract fun moviesDao(): MoviesDao
}