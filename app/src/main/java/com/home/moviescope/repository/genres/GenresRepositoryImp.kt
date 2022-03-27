package com.home.moviescope.repository.genres

import com.home.moviescope.model.Genres

/**
отдеьлный репозиторий с кодировкой и расшифровкой жанров
 */
class GenresRepositoryImp : GenresRepository {
    override fun getGenresFromServer(): MutableList<Genres> {
        return mutableListOf()
    }
}