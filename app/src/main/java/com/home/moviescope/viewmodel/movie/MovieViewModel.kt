package com.home.moviescope.viewmodel.movie

import android.icu.text.CaseMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.model.Category
import com.home.moviescope.model.Movie
import com.home.moviescope.model.Repo
import com.home.moviescope.model.Repository

class MovieViewModel () : ViewModel() {
    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    fun setMovie(movie: Movie) {
        _movie.value = movie
    }
}