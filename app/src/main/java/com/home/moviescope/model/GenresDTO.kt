package com.home.moviescope.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenresDTO(
    var genres: ArrayList<Genres> = arrayListOf()
) : Parcelable
