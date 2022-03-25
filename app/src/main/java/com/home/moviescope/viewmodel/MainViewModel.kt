package com.home.moviescope.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.repository.categoryRepo.Repo
import com.home.moviescope.repository.categoryRepo.Repository


class MainViewModel(
    private val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: Repo = Repository()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserver
    fun getCategoryFromRemoteSource() = getDataFromRemoteSource()

    private fun getDataFromRemoteSource() {
        liveDataToObserver.postValue(
            AppState.SuccessInit(
                repository.getCategoryFromServer()
            )
        )

    }
}