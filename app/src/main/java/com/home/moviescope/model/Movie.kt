package com.home.moviescope.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val title: String = "default title",
    val genre: String = "Horror"
    //задел на будущее
    // val poster:Image,
    // val rating:Int,
    // val description:String,
    // val credits:List<Staff> - еще 1 дата класс?
): Parcelable


