package com.home.moviescope.viewmodel.movie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.BuildConfig
import com.home.moviescope.app.App
import com.home.moviescope.model.Category
import com.home.moviescope.model.CategoryDTO
import com.home.moviescope.model.Genres
import com.home.moviescope.model.GenresDTO
import com.home.moviescope.repository.GenresRepository
import com.home.moviescope.repository.GenresRepositoryImp
import com.home.moviescope.repository.movierepo.GetMovies
import com.home.moviescope.repository.movierepo.MovieRepository
import com.home.moviescope.repository.RemoteDataSource
import com.home.moviescope.room.LocalRepository
import com.home.moviescope.room.LocalRepositoryImp
import com.home.moviescope.utils.convertDTOtoGenresList
import com.home.moviescope.utils.convertDTOtoMovieList
import com.home.moviescope.utils.fillCategory
import com.home.moviescope.utils.fillGenresList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class MovieRepositoryViewModel(

    private val movieRepository: GetMovies = MovieRepository(RemoteDataSource()),
    private val genresRepository: GenresRepository = GenresRepositoryImp(),
    private val localgenreRepository: LocalRepository = LocalRepositoryImp(App.getGenresDao())

) : ViewModel() {
    lateinit var categoryAdapter: com.home.moviescope.recycler.CategoryAdapter

    private val _categoryList = MutableLiveData<List<Category>>(emptyList())
    private val _genresList = MutableLiveData<MutableList<Genres>>()

    fun setList(categoryList: List<Category>) {
        _categoryList.value = categoryList
    }

    fun setGenreList(genreList: MutableList<Genres>){
        _genresList.value = genreList
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
                fillCategory(
                    _categoryList.value!![_id],
                    convertDTOtoMovieList(serverResponse),
                    categoryAdapter
                )
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

    /**
     * полчучаем список жанров
     */
    private val callBack = object : Callback<GenresDTO> {
        override fun onResponse(call: Call<GenresDTO>, response: Response<GenresDTO>) {
            val serverResponseGenre: GenresDTO? = response.body()
            if (response.isSuccessful && serverResponseGenre != null) {
                Log.d("testdb", "got response")
                checkResponseGenre(serverResponseGenre)
            } else
                Log.d("GENRE", REQUEST_ERROR)
        }

        private fun checkResponseGenre(serverResponseGenre: GenresDTO) {
            val genres = serverResponseGenre.genres
            if (genres.size == 0) {
                Log.d("GENRE", CORRUPTED_DATA)
            } else {
                Log.d("testdb", "GOT GENRE DATA in response = " + genres.toString())
                if (_genresList.value?.size==0) {
                    _genresList.value=genresRepository.getGenresFromServer()
                }

              setGenreList(genres as MutableList<Genres>)
                Log.d("testdb","check " + _genresList.value.toString() )

                localgenreRepository.clearGenreEntity()
                for (i in genres.indices) {
                   saveGenreToDB(genres[i])
                }

            }
        }

        override fun onFailure(call: Call<GenresDTO>, t: Throwable) {
            Log.d("GENRE", REQUEST_ERROR)
        }

    }

    fun getGenresFromServer(language: String) {
        movieRepository.getGenresFromServer(
            BuildConfig.MOVIEDB_API_KEY,
            language,
            callBack
        )
    }

    /**
     * сохраняем жанр фильма в БД
     */

    fun saveGenreToDB(genres: Genres) {

        localgenreRepository.saveEntity(genres)
    }

}