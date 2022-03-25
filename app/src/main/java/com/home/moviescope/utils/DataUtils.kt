package com.home.moviescope.utils

import android.os.Handler
import android.util.Log
import com.google.gson.Gson
import com.home.moviescope.BuildConfig
import com.home.moviescope.model.*
import com.home.moviescope.recycler.CategoryAdapter
import com.home.moviescope.room.Genres.GenresEntity
import com.home.moviescope.room.Movies.MoviesEntity
import okhttp3.*
import java.io.IOException
import java.util.ArrayList


const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/w300"

fun convertDTOtoMovieList(categoryDTO: CategoryDTO): MutableList<Movie> {
    /**
     * так как наши данные по жанрам это массив закодированых ID
     * (раскодируем позже пока не придумал как)
     * то для надежности пробегаемся по всем полученным данным
     * и добавляем эти объекты по однному
     * затем создаем Фильм и добавляем к нашей категории
     */
    var movieList: MutableList<Movie> = mutableListOf()
    for (i in categoryDTO.results.indices) {
        var ids: ArrayList<Int> = arrayListOf()
        for (j in categoryDTO.results[i].genre_ids.indices) {
            ids.add(categoryDTO.results[i].genre_ids[j])
        }
        var movie = Movie(
            ids,
            categoryDTO.results[i].title,
            categoryDTO.results[i].overview,
            BASE_POSTER_URL + categoryDTO.results[i].poster_path
        )
        Log.d("@@@@", movie.title!!)
        movieList.add(
            movie
        )
    }
    return movieList
}

fun fillCategory(category: Category, list: MutableList<Movie>, categoryAdapter: CategoryAdapter) {
    category.members.clear()
    category.members.addAll(list)
    categoryAdapter.notifyDataSetChanged()
}

fun convertDTOtoGenresList(genreDTO: GenresDTO): MutableList<Genres> {
    var genresList: MutableList<Genres> = mutableListOf()
    for (i in genreDTO.genres.indices) {
        genresList.add(genreDTO.genres[i])
    }
    Log.d("testdb","convert genres "+ genresList.toString())
    return genresList
}



fun fillGenresList(genresRepo: MutableList<Genres>, list: MutableList<Genres>) {
    genresRepo.clear()
    genresRepo.addAll(list)
    Log.d("testdb", "fill genre repo " + genresRepo.toString())
}


fun convertMoviesEntityToMovie(entityList: List<MoviesEntity>): List<Movie> {
    return entityList.map {
        Movie(arrayListOf(), it.title, it.overview, it.poster_path)
    }
}

fun convertMovieToMoviesEntity(movie: Movie): MoviesEntity {
    return MoviesEntity(0, movie.title!!, movie.overview!!, movie.poster_path!!, 0)
}


fun convertGenresEntityToGenre(entityList: List<GenresEntity>): List<Genres> {
    return entityList.map {
        Genres(it.code, it.genre)
    }
}

fun convertGenreToGenreEntity(genre: Genres): GenresEntity {
    return GenresEntity(0, genre.id, genre.name)
}


