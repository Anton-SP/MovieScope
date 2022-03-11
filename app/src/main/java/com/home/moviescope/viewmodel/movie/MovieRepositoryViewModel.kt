package com.home.moviescope.viewmodel.movie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.BuildConfig
import com.home.moviescope.model.CategoryDTO
import com.home.moviescope.model.Movie
import com.home.moviescope.repository.GetMovies
import com.home.moviescope.repository.MovieRepository
import com.home.moviescope.repository.RemoteDataSource
import com.home.moviescope.utils.convertDTOtoMovieList
import com.home.moviescope.viewmodel.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class MovieRepositoryViewModel(
    val movieRepoLiveData: MutableLiveData<AppState> = MutableLiveData(),
    val responseLiveData: MutableLiveData<MutableList<Movie>> = MutableLiveData<MutableList<Movie>>(),
    private val movieRepository: GetMovies = MovieRepository(RemoteDataSource())

): ViewModel() {

    private val callback = object : Callback<CategoryDTO> {
        override fun onResponse(call: Call<CategoryDTO>, response: Response<CategoryDTO>) {
            val serverResponse : CategoryDTO? = response.body()
            Log.d("RETROFIT","get response RETROfit")
            movieRepoLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    Log.d("RETROFIT","GOT response RETROfit")
                    checkResponse(serverResponse)
                } else {
                    Log.d("RETROFIT","empty response RETROfit")
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<CategoryDTO>, t: Throwable) {
            movieRepoLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

    }

    private fun checkResponse(serverResponse: CategoryDTO): AppState {
    val results = serverResponse.results
        return  if (results.size == 0 ) {
            AppState.Error(Throwable(CORRUPTED_DATA))
        } else {
            Log.d("RETROFIT","LOAD MOVIES IN RETROfit = " +results[0].title)
            responseLiveData.postValue(convertDTOtoMovieList(serverResponse))
            AppState.SuccessLoad(convertDTOtoMovieList(serverResponse))
        }
    }

    fun getMovieFromRemoteSource(requestEndpoint:String?,language:String,pages:Int) {
        Log.d("RETROFIT","LOAD MOVIES IN RETROfit")
        movieRepoLiveData.value = AppState.Loading
        movieRepository.getMoviesFromServer(requestEndpoint,BuildConfig.MOVIEDB_API_KEY,language,pages, callback)
    }

}