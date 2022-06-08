package com.home.moviescope.room.Genres

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GenresEntity(
    /**
     * в последствии думаю нужно избавляться от autoGenerate
     * id фильмов и жанров идут не попорядку и как следствие куча костылей (ну мы же учимся)
     * и если делать к примеру просто поиск по сущности то он будет сравнивать поле id
     * а не то что сущнось собой представляет как в принципе и пололжено
     */
    @PrimaryKey(autoGenerate = true)
    val id_genre: Long = 0,
    val code: Int = 0,
    val genre: String = ""

)