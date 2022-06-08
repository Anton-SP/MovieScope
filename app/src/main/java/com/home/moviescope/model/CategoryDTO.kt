package com.home.moviescope.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryDTO(
    /**
     * из API results определяется как список фильмов
     */
    var results: ArrayList<Results> = arrayListOf()
):Parcelable

@Parcelize
data class Results(
    val genre_ids: ArrayList<Int> = arrayListOf(),
    val title: String? = null,
    val overview: String? = null,
    val poster_path: String? = null,
):Parcelable
