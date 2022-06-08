package com.home.moviescope.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.app.App
import com.home.moviescope.model.Movie
import com.home.moviescope.room.LocalRepository
import com.home.moviescope.room.LocalRepositoryImp
import com.home.moviescope.viewmodel.AppState

class DBViewModel(
    val genresAndMoviesLiveData: MutableLiveData<AppState> = MutableLiveData(),
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

}