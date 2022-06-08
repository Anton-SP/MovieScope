package com.home.moviescope.model

data class Category(
    val name: String = "Latest",
    val members: List<Movie> = getDefaultMovieList() //заглушка с дефолтными значениями
)
/**
 * для теста создется дефолтный набор фильмов
 */
fun getDefaultMovieList(): List<Movie> {
    return listOf(
        Movie("movie 1", "Horror"),
        Movie("movie 2", "Comedy"),
        Movie("movie 3", "Action"),
        Movie("movie 4", "Bio")
    )
}
