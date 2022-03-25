package com.home.moviescope.repository.movierepo

import com.home.moviescope.model.CategoryDTO
import com.home.moviescope.model.GenresDTO
import com.home.moviescope.repository.RemoteDataSource
import retrofit2.Callback

class MovieRepository(private val remoteDataSource: RemoteDataSource): GetMovies
{
    override fun getMoviesFromServer(
        requestEndpoint: String?,
        token: String,
        language: String,
        pages: Int,
        callback: Callback<CategoryDTO>
    ) {
        remoteDataSource.getMovies(requestEndpoint,token,language,pages,callback)

    }

    override fun getGenresFromServer(
        token: String,
        language: String,
        callback: Callback<GenresDTO>
    ) {
        remoteDataSource.getGenres(token,language,callback)
    }
}