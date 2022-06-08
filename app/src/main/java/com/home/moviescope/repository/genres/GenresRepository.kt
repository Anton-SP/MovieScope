package com.home.moviescope.repository.genres

import com.home.moviescope.model.Genres

interface GenresRepository {
    fun getGenresFromServer():MutableList<Genres>
}