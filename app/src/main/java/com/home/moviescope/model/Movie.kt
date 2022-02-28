package com.home.moviescope.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Movie(
    var genreIds: ArrayList<Int> = arrayListOf(),
    var title: String? = null,
    var overview: String? = null,
    //задел на будущее
    // val poster:Image,
    // val rating:Int,
    // val description:String,
    // val credits:List<Staff> - еще 1 дата класс?
)