package com.home.moviescope.repository

import com.home.moviescope.model.Genres

interface GenresRepository {
    fun getGenresFromServer():MutableList<Genres>
}