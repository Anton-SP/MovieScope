package com.home.moviescope.room.Movies

import androidx.room.*

@Dao
interface MoviesDao {
    @Query("SELECT * FROM MoviesEntity")
    suspend fun all(): List<MoviesEntity>

    @Query("SELECT * FROM MoviesEntity WHERE title LIKE :title")
    suspend fun getDataByWord(title: String): List<MoviesEntity>

    @Query("SELECT EXISTS(SELECT * FROM MoviesEntity WHERE title = :title)")
    fun isExists(title:String): Boolean

    @Query("SELECT * FROM MoviesEntity WHERE title = :title")
    fun findByTitle(title:String): MoviesEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MoviesEntity)

    @Update
    suspend fun update(entity: MoviesEntity)

    @Delete
    suspend fun delete(entity: MoviesEntity)

    @Query("DELETE FROM MoviesEntity")
    suspend fun deleteAll()

    @Transaction
    suspend fun checkAndInsert(entity: MoviesEntity){
        if (!isExists(entity.title)) {
           insert(entity)
        }
    }

    @Transaction
    suspend fun checkAndDelete(entity: MoviesEntity){
        if (isExists(entity.title)) {
            delete(findByTitle(entity.title))
        }
    }


}