package com.home.moviescope.room.Genres

import androidx.room.*
import com.home.moviescope.room.Movies.MoviesEntity

@Dao
interface GenresDao {
    @Query("SELECT * FROM GenresEntity")
    fun all(): List<GenresEntity>

    @Query("SELECT * FROM GenresEntity WHERE genre LIKE :genre")
    fun getDataByWord(genre: String): List<GenresEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: GenresEntity)

    @Update
    fun update(entity: GenresEntity)

    @Delete
    fun delete(entity: GenresEntity)

    @Query("DELETE FROM GenresEntity")
    fun deleteAll()
}