package com.home.moviescope.viewmodel.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.BuildConfig
import com.home.moviescope.model.Category
import com.home.moviescope.model.CategoryDTO
import com.home.moviescope.repository.GetMovies
import com.home.moviescope.repository.MovieRepository
import com.home.moviescope.repository.RemoteDataSource
import com.home.moviescope.utils.convertDTOtoMovieList
import com.home.moviescope.utils.fillCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class MovieRepositoryViewModel(

    private val movieRepository: GetMovies = MovieRepository(RemoteDataSource())

) : ViewModel() {
    lateinit var categoryAdapter: com.home.moviescope.recycler.CategoryAdapter

    private val _categoryList = MutableLiveData<List<Category>>(emptyList())
    val categoryList: LiveData<List<Category>>
        get() = _categoryList

    fun setList(categoryList: List<Category>) {
        _categoryList.value = categoryList
    }

    /**
     * вложенный класс кастомного колбэка
     * по сути мне нужно было прокинуть id категории к которй подгружаем фильмы
     * ну вот только из за этого он и реализован
     */
    inner class customRetrofitCallback<T>(id: Int) : Callback<CategoryDTO> {
        var _id = id

        override fun onResponse(call: Call<CategoryDTO>, response: Response<CategoryDTO>) {
            var id = _id
            val serverResponse: CategoryDTO? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                checkResponse(serverResponse, id)
            } else {
                Log.d("RETROFIT", REQUEST_ERROR)
            }
        }

        private fun checkResponse(serverResponse: CategoryDTO, id: Int) {
            val results = serverResponse.results
            if (results.size == 0) {
                Log.d("RETROFIT", CORRUPTED_DATA)
            } else {
                fillCategory(_categoryList.value!![_id], convertDTOtoMovieList(serverResponse),categoryAdapter)
            }
        }

        override fun onFailure(call: Call<CategoryDTO>, t: Throwable) {
            Log.d("RETROFIT", REQUEST_ERROR)
        }
    }

    fun getMovieFromRemoteSource(requestEndpoint: String?, language: String, pages: Int, id: Int) {
        movieRepository.getMoviesFromServer(
            requestEndpoint,
            BuildConfig.MOVIEDB_API_KEY,
            language,
            pages,
            customRetrofitCallback<CategoryDTO>(id)
        )
    }

}