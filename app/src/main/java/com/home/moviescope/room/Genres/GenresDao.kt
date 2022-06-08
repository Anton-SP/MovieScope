package com.home.moviescope.room.Genres

import androidx.room.*
import com.home.moviescope.model.Movie
import com.home.moviescope.room.Movies.MoviesEntity

@Dao
interface GenresDao {
    @Query("SELECT * FROM GenresEntity")
    suspend fun all(): List<GenresEntity>

    @Query("SELECT * FROM GenresEntity WHERE genre LIKE :genre")
    suspend fun getDataByWord(genre: String): List<GenresEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GenresEntity)

    @Update
    suspend fun update(entity: GenresEntity)

    @Delete
    suspend fun delete(entity: GenresEntity)

    @Query("DELETE FROM GenresEntity")
    suspend fun deleteAll()

    /**
     * используется при смене языка
     * вычищаем старые данные и подгружаем нужные
     *
     */
    @Transaction
    suspend fun deleteAndInsertAll(entityList:List<GenresEntity>){
        deleteAll()
        for (i in entityList.indices){
            insert(entityList[i])
        }
    }

}