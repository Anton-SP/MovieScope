package com.home.moviescope.utils

import android.util.Log
import com.home.moviescope.model.*
import com.home.moviescope.recycler.CategoryAdapter
import com.home.moviescope.room.Genres.GenresEntity
import com.home.moviescope.room.Movies.MoviesEntity
import java.util.ArrayList


const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/w300"

/**
 * конвертация
 *      конвертация
 *          конвертация
 */
fun convertDTOtoMovieList(categoryDTO: CategoryDTO): MutableList<Movie> {
    /**
     *
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

fun setGenre(listMovie: MutableList<Movie>, listGenre: MutableList<Genres>) {
    for (i in listMovie.indices) {
        listMovie[i].genreString = convertGenreCodeToNames(listMovie[i].genreIds, listGenre)
    }
}

fun fillCategory(category: Category, list: MutableList<Movie>, categoryAdapter: CategoryAdapter) {
    category.members.clear()
    category.members.addAll(list)
    categoryAdapter.notifyDataSetChanged()
}

fun convertMoviesEntityToMovie(entityList: List<MoviesEntity>): MutableList<Movie> {
    return entityList.map {
        Movie(arrayListOf(), it.title, it.overview, it.poster_path,it.genreString)
    }.toMutableList()
}

fun convertMovieToMoviesEntity(movie: Movie): MoviesEntity {
    return MoviesEntity(0, movie.title!!, movie.overview!!, movie.poster_path!!, movie.genreString)
}

fun convertGenresEntityToGenre(entityList: List<GenresEntity>): List<Genres> {
    return entityList.map {
        Genres(it.code, it.genre)
    }
}

fun convertGenreToGenreEntity(genre: Genres): GenresEntity {
    return GenresEntity(0, genre.id, genre.name)
}

fun convertGenresListToEntityList(genres: List<Genres>): List<GenresEntity> {
var resListEntity:MutableList<GenresEntity> = mutableListOf()
        for (i in genres.indices){
            resListEntity.add(i, GenresEntity(0,genres[i].id,genres[i].name))
        }
    return resListEntity
}

fun convertGenreCodeToNames(genreId: ArrayList<Int>, genreList: List<Genres>): String {
    var resGenreString: String = ""
    for (i in genreId.indices) {
        for (j in genreList.indices) {
            if (genreId[i] == (genreList[j].id)) {
                resGenreString = resGenreString + genreList[j].name + " "
            }
        }
    }
    return resGenreString
}