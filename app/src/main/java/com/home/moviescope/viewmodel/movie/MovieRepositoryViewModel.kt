package com.home.moviescope.viewmodel.movie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.home.moviescope.BuildConfig
import com.home.moviescope.app.App
import com.home.moviescope.model.*
import com.home.moviescope.repository.genres.GenresRepository
import com.home.moviescope.repository.genres.GenresRepositoryImp
import com.home.moviescope.repository.movierepo.GetMovies
import com.home.moviescope.repository.movierepo.MovieRepository
import com.home.moviescope.repository.RemoteDataSource
import com.home.moviescope.room.LocalRepository
import com.home.moviescope.room.LocalRepositoryImp
import com.home.moviescope.utils.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class MovieRepositoryViewModel(

    private val movieRepository: GetMovies = MovieRepository(RemoteDataSource()),
    private val genresRepository: GenresRepository = GenresRepositoryImp(),
    private val localRepository: LocalRepository = LocalRepositoryImp(
        App.getGenresDao(),
        App.getMoviesDao()
    )

) : ViewModel() {
    lateinit var categoryAdapter: com.home.moviescope.recycler.CategoryAdapter

    private val _categoryList = MutableLiveData<List<Category>>(emptyList())
    private val _genresList = MutableLiveData<MutableList<Genres>>()

    fun setList(categoryList: List<Category>) {
        _categoryList.value = categoryList
    }

    fun setGenreList(genreList: MutableList<Genres>) {
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
                var listMovie = convertDTOtoMovieList(serverResponse)
                _genresList.value?.let { setGenre(listMovie, it) }
                fillCategory(
                    _categoryList.value!![_id],
                    listMovie,
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
     * про корутины оочень поверхностно почитал но я так понял работают они асинхронно
     * и очень дружат с рум и вью моделью
     * всем сетевым функциям добавляем suspend
     * и цепочку запускаем в корутине через viewModelScope{}
     * еще раз в очень общих словах...
     * и да, так кода меньше не надо выдумыать сервисы потоки и колбэки
     */
    private val callBack = object : Callback<GenresDTO> {
        override fun onResponse(call: Call<GenresDTO>, response: Response<GenresDTO>) {
            val serverResponseGenre: GenresDTO? = response.body()
            if (response.isSuccessful && serverResponseGenre != null) {
                Log.d("testdb", "got response")
                this@MovieRepositoryViewModel.viewModelScope.launch {
                    checkResponseGenre(
                        serverResponseGenre
                    )
                }
            } else
                Log.d("GENRE", REQUEST_ERROR)
        }

        private suspend fun checkResponseGenre(serverResponseGenre: GenresDTO) {
            val genres = serverResponseGenre.genres
            if (genres.size == 0) {
                Log.d("GENRE", CORRUPTED_DATA)
            } else {
                if (_genresList.value?.size == 0) {
                    _genresList.value = genresRepository.getGenresFromServer()
                }
                setGenreList(genres as MutableList<Genres>)
                /**
                 * очищаем таблицу и заполняем новыми даными
                 */
                localRepository.deleteAndInsertAll(genres)
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

    suspend fun saveMovieToDB(movie: Movie) {
        localRepository.checkAndInsert(movie)
    }

}