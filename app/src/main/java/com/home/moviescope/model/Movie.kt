package com.home.moviescope.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Movie(
    var genreIds: ArrayList<Int> = arrayListOf(),
    var title: String? = null,
    var overview: String? = null,
    val poster_path: String? = null
)
