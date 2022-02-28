package com.home.moviescope.model

data class Category(
    val name: String = "Latest",
    val requestName:String?,
 //   val members: List<Movie> = getDefaultMovieList() //заглушка с дефолтными значениями
    var members: MutableList<Movie> = mutableListOf() //
)
/**
 * для теста создется дефолтный набор фильмов
 */
fun getDefaultMovieList(): List<Movie> {
    return listOf(
        Movie(arrayListOf(1,1,1,), "movie 1","ОПИСАНИЕ 1"),
        Movie(arrayListOf(2,2,2,), "movie 2","ОПИСАНИЕ 2"),
        Movie(arrayListOf(3,3,3,), "movie 3","ОПИСАНИЕ 3"),
        Movie(arrayListOf(4,4,4,), "movie 3","ОПИСАНИЕ 4"),

    )
}
