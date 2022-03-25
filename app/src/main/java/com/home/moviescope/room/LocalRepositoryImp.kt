package com.home.moviescope.room

import com.home.moviescope.model.Genres
import com.home.moviescope.model.Movie
import com.home.moviescope.room.Genres.GenresDao
import com.home.moviescope.room.Movies.MoviesDao
import com.home.moviescope.utils.convertGenreToGenreEntity
import com.home.moviescope.utils.convertGenresEntityToGenre
import com.home.moviescope.utils.convertMoviesEntityToMovie

class LocalRepositoryImp(private val localDataSource: GenresDao):LocalRepository {
   /* override fun getAllMovies(): List<Movie> {
        return convertMoviesEntityToMovie(localDataSource.all())
    }*/



  /*  override fun saveEntity(movie: Movie) {
        localDataSource.insert(convertMovieToMoviesEntity(movie))
    }*/

    override fun getAllGenres(): List<Genres> {
       return convertGenresEntityToGenre(localDataSource.all())
    }

    override fun saveEntity(genre: Genres) {
        localDataSource.insert(convertGenreToGenreEntity(genre))
    }

    override fun clearGenreEntity() {
       localDataSource.deleteAll()
    }
}

/*
class LocalRepositoryImp(private val localDataSource: MoviesDao):LocalRepository {
    override fun getAllMovies(): List<Movie> {
        return convertMoviesEntityToMovie(localDataSource.all())
    }



    override fun saveEntity(movie: Movie) {
        localDataSource.insert(convertMovieToMoviesEntity(movie))
    }
}*/
