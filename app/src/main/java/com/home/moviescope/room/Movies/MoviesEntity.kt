package com.home.moviescope.room.Movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.home.moviescope.room.Genres.GenresEntity

@Entity
data class MoviesEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val overview: String = "",
    val poster_path: String = "",
    @ColumnInfo(name = "genre_id")
    val genresId: Int = 0

)
