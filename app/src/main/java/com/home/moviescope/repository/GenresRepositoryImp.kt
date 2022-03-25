package com.home.moviescope.repository

import com.home.moviescope.model.Genres

class GenresRepositoryImp : GenresRepository  {
    override fun getGenresFromServer(): MutableList<Genres> {
        return mutableListOf()
    }
}