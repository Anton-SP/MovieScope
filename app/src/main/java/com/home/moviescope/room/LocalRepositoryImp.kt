package com.home.moviescope.room

import com.home.moviescope.model.Genres
import com.home.moviescope.model.Movie
import com.home.moviescope.room.Genres.GenresDao
import com.home.moviescope.room.Movies.MoviesDao
import com.home.moviescope.utils.*

class LocalRepositoryImp(
    private val localDataSourceGenre: GenresDao,
    private val localDataSourceMovie: MoviesDao
) : LocalRepository {

    override suspend fun getAllGenres(): List<Genres> {
        return convertGenresEntityToGenre(localDataSourceGenre.all())
    }

    override suspend fun saveGenreEntity(genre: Genres) {
        localDataSourceGenre.insert(convertGenreToGenreEntity(genre))
    }

    override suspend fun clearGenreEntity() {
        localDataSourceGenre.deleteAll()
    }

    override suspend fun deleteAndInsertAll(genres: List<Genres>) {
        localDataSourceGenre.deleteAndInsertAll(convertGenresListToEntityList(genres))
    }

    override suspend fun getAllMovies(): MutableList<Movie> {
        return convertMoviesEntityToMovie(localDataSourceMovie.all())
    }

    override suspend fun saveMovieEntity(movie: Movie) {
        localDataSourceMovie.insert(convertMovieToMoviesEntity(movie))
    }

    override suspend fun clearMovieEntity() {
        localDataSourceMovie.deleteAll()
    }

    override suspend fun checkAndInsert(movie: Movie) {
        localDataSourceMovie.checkAndInsert(convertMovieToMoviesEntity(movie))
    }

    override suspend fun deleteMovieEntity(movie: Movie) {
        localDataSourceMovie.delete(convertMovieToMoviesEntity(movie))
    }

    override suspend fun checkAndDelete(movie: Movie) {
        localDataSourceMovie.checkAndDelete(convertMovieToMoviesEntity(movie))
    }

}

