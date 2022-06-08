package com.home.moviescope.model

data class CategoryDTO(
    /**
     * из API results определяется как список фильмов
     */
    var results: ArrayList<Results> = arrayListOf()
)

data class Results(
    val genre_ids: ArrayList<Int> = arrayListOf(),
    val title: String? = null,
    val overview: String? = null,
)
