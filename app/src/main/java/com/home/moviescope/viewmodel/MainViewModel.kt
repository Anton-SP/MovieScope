package com.home.moviescope.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.model.Repo
import com.home.moviescope.model.Repository
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: Repo = Repository()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserver
    fun getCategoryFromRemoteSource() = getDataFromRemoteSource()

    private fun getDataFromRemoteSource() {
        liveDataToObserver.value = AppState.Loading
        var random = (0..10).random() // generated random from 0 to 10 included
        if (random > 0) {
            Thread {
                sleep(2000)
                liveDataToObserver.postValue(
                    AppState.Success(
                        repository.getCategoryFromServer()
                    )
                )
            }.start()
        } else {
            Thread {
                sleep(2000)
                liveDataToObserver.postValue(AppState.Error)
            }.start()
        }
    }

}
