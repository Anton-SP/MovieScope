package com.home.moviescope.viewmodel

import com.home.moviescope.model.Category
import com.home.moviescope.model.Movie

sealed class AppState {
    data class SuccessInit(val categoryData: List<Category>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}