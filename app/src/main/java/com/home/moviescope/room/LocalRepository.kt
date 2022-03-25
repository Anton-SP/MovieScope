package com.home.moviescope.room

import com.home.moviescope.model.Genres
import com.home.moviescope.model.Movie

interface LocalRepository {
    fun getAllGenres():List<Genres>
    fun saveEntity(genre: Genres)
    fun clearGenreEntity()
  /*  fun getAllMovies():List<Movie>
    fun saveEntity(movie: Movie)*/
}