package com.home.moviescope.room.Genres

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GenresEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: Int = 0,
    val genre: String = ""

)