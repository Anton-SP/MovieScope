package com.home.moviescope.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.app.App
import com.home.moviescope.model.Genres
import com.home.moviescope.model.Movie
import com.home.moviescope.room.LocalRepository
import com.home.moviescope.room.LocalRepositoryImp
import com.home.moviescope.viewmodel.AppState

class DBViewModel(
    val genresAndMoviesLiveData: MutableLiveData<AppState> = MutableLiveData(),
    val _filtredList: MutableLiveData<MutableList<Movie>> = MutableLiveData<MutableList<Movie>>(),
private val localRepository: LocalRepository = LocalRepositoryImp(
    App.getGenresDao(),
    App.getMoviesDao()
)
) : ViewModel() {


    suspend fun getAllData() {
        genresAndMoviesLiveData.value = AppState.SuccessInitDB(
            localRepository.getAllGenres(),
            localRepository.getAllMovies()
        )
    }

    suspend fun removeMovieFromDB(movie: Movie) {
        localRepository.checkAndDelete(movie)
    }

    suspend fun getMovieList(): MutableList<Movie> {
        return localRepository.getAllMovies()
    }

    suspend fun getFilterList(search: String) {
        localRepository.getDataByWord(search)?.let {
            setFiltredList(it)
        }

    }

    fun setFiltredList(filtredList: MutableList<Movie>) {
        _filtredList.value = filtredList
    }

}