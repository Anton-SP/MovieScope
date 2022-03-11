package com.home.moviescope.repository

import android.view.textclassifier.ConversationActions
import com.home.moviescope.model.CategoryDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviedbAPI {
@GET("{request_endpoint}")
//@GET("now_playing")
fun getMovies(
    @Path("request_endpoint") requestEndpoint: String?,
    @Query("api_key") token: String,
    @Query("language") language: String,
    @Query("page") pages: Int
) : Call<CategoryDTO>
}
