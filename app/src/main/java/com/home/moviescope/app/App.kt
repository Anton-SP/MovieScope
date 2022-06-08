package com.home.moviescope.app

import android.app.Application
import androidx.room.Room
import com.home.moviescope.room.Genres.GenresDao
import com.home.moviescope.room.Movies.MoviesDao
import com.home.moviescope.room.MoviesDataBase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var db: MoviesDataBase? = null
        private const val DB_NAME = "Movies.db"

        fun getGenresDao(): GenresDao {
            if (db == null) {
                synchronized(MoviesDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw  IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            MoviesDataBase::class.java, DB_NAME
                        )
                            .build()
                    }
                }
            }
            return db!!.genresDao()
        }

        fun getMoviesDao():MoviesDao{
            if (db == null) {
                synchronized(MoviesDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw  IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            MoviesDataBase::class.java, DB_NAME
                        )
                            .build()
                    }
                }
            }
            return db!!.moviesDao()
        }
    }

}