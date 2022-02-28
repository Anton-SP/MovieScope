package com.home.moviescope.model

data class CategoryDTO(
    /**
     * из API results определяется как список фильмов
     */

    var results: ArrayList<Results> = arrayListOf()
)

data class Results(
    val genreIds: ArrayList<Int> = arrayListOf(),
    val title: String? = null,
    val overview: String? = null,
)
