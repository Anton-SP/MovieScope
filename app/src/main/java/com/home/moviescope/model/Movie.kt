package com.home.moviescope.model

import android.os.Parcelable
import androidx.room.Relation
import com.home.moviescope.room.Genres.GenresEntity
import kotlinx.android.parcel.Parcelize

data class Movie(

    var genreIds: ArrayList<Int> = arrayListOf(),
    var title: String? = null,
    var overview: String? = null,
    val poster_path: String? = null,
    var genreString:String = ""
/**
где то при конвертации теряется строка genreString при отображении watch list
 в бд она есть а при загрузке в ui пустое поле
 */
)
