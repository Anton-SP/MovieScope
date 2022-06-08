package com.home.moviescope.viewmodel

import com.home.moviescope.model.Category

sealed class AppState {
    data class Success(val categoryData: List<Category>) : AppState()
    object Error: AppState()
    object Loading : AppState()
}