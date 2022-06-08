package com.home.moviescope.repository.movierepo

import com.home.moviescope.model.CategoryDTO
import com.home.moviescope.model.GenresDTO
import com.home.moviescope.model.Movie
import retrofit2.Callback
import retrofit2.http.Query

interface GetMovies {
    fun getMoviesFromServer(
        requestEndpoint: String?,
        token:String,
        language: String,
        pages: Int,
        callback: Callback<CategoryDTO>
    )

    fun getGenresFromServer(
        token:String,
        language: String,
        callback: Callback<GenresDTO>
    )
}