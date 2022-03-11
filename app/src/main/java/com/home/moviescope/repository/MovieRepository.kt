package com.home.moviescope.repository

import com.home.moviescope.model.CategoryDTO
import retrofit2.Callback

class MovieRepository(private val remoteDataSource: RemoteDataSource):GetMovies
{
    override fun getMoviesFromServer(
        requestEndpoint: String?,
        token: String,
        language: String,
        pages: Int,
        callback: Callback<CategoryDTO>
    ) {
        remoteDataSource.getGetMovies(requestEndpoint,token,language,pages,callback)
    }
}