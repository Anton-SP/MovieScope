package com.home.moviescope.room.Movies

import androidx.room.*

@Dao
interface MoviesDao {
    @Query("SELECT * FROM MoviesEntity")
    fun all(): List<MoviesEntity>

    @Query("SELECT * FROM MoviesEntity WHERE title LIKE :title")
    fun getDataByWord(title: String): List<MoviesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: MoviesEntity)

    @Update
    fun update(entity: MoviesEntity)

    @Delete
    fun delete(entity: MoviesEntity)
}