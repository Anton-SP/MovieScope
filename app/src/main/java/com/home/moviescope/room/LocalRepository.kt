package com.home.moviescope.room

import com.home.moviescope.model.Genres
import com.home.moviescope.model.Movie
import com.home.moviescope.room.Movies.MoviesEntity

interface LocalRepository {
    suspend fun getAllGenres(): List<Genres>
    suspend fun saveGenreEntity(genre: Genres)
    suspend fun clearGenreEntity()
    suspend fun deleteAndInsertAll(genres: List<Genres>)

    suspend fun getAllMovies(): MutableList<Movie>
    suspend fun saveMovieEntity(movie: Movie)
    suspend fun clearMovieEntity()
    suspend fun checkAndInsert(movie: Movie)
    suspend fun deleteMovieEntity(movie: Movie)
    suspend fun checkAndDelete(movie: Movie)
    suspend fun getDataByWord(search: String): MutableList<Movie>

}