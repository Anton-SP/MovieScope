package com.home.moviescope.repository

import com.google.gson.GsonBuilder
import com.home.moviescope.model.CategoryDTO
import com.home.moviescope.model.GenresDTO
import com.home.moviescope.repository.movierepo.MoviedbAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import java.io.IOException

const val BASE_URL = "https://api.themoviedb.org/3/"

class RemoteDataSource() {
    private val moviedbAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create())
        )
        .client(createOkHttpClient(MovieApiInterceptor()))
        .build().create(MoviedbAPI::class.java)

    fun getMovies(
        requestEndpoint: String?, token: String, language: String,
        pages: Int,
        callback: Callback<CategoryDTO>
    ) {
        moviedbAPI.getMovies(
            requestEndpoint,
            token,
            language,
            pages
        ).enqueue(callback)
    }

    fun getGenres(
        token: String,language: String,callback: Callback<GenresDTO>
    ) {
        moviedbAPI.getGenres(token,language).enqueue(callback)
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    inner class MovieApiInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain.proceed(chain.request())
        }
    }

}