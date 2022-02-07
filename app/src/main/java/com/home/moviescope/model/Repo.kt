package com.home.moviescope.model

//Создать Object.
object Repo {
    private val movieList: MutableList<Movie> = listOf<Movie>().toMutableList()

    fun addMovie(movie: Movie) {
        movieList.add(movie)
    }

    fun getMovieList(): MutableList<Movie> {
        return movieList
    }

}
