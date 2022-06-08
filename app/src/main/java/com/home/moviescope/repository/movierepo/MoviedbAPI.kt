package com.home.moviescope.repository.movierepo

import android.view.textclassifier.ConversationActions
import com.home.moviescope.model.CategoryDTO
import com.home.moviescope.model.GenresDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviedbAPI {
@GET("movie/{request_endpoint}")
fun getMovies(
    @Path("request_endpoint") requestEndpoint: String?,
    @Query("api_key") token: String,
    @Query("language") language: String,
    @Query("page") pages: Int
) : Call<CategoryDTO>

@GET("genre/movie/list")
fun getGenres(
    @Query("api_key") token: String,
    @Query("language") language: String,
) : Call<GenresDTO>



}
