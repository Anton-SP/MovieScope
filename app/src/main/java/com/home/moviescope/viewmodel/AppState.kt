package com.home.moviescope.viewmodel

import com.home.moviescope.model.Category
import com.home.moviescope.model.Genres
import com.home.moviescope.model.Movie

sealed class AppState {
    data class SuccessInit(val categoryData: List<Category>) : AppState()
    data class SuccessInitDB(val genresData: List<Genres>, val moviesData: MutableList<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}