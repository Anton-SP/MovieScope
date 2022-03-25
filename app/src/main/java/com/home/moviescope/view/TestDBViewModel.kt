package com.home.moviescope.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.app.App
import com.home.moviescope.room.LocalRepository
import com.home.moviescope.room.LocalRepositoryImp
import com.home.moviescope.viewmodel.AppState

class TestDBViewModel(
    val genresLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val localgenreRepository: LocalRepository = LocalRepositoryImp(App.getGenresDao())
) : ViewModel() {

    fun getAllGenres() {
      //  genresLiveData.value = AppState.Loading
        genresLiveData.value = AppState.SuccessInitDB(localgenreRepository.getAllGenres())
    }
}