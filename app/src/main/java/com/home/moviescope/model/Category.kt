package com.home.moviescope.model

data class Category(
    val name: String = "Latest",
    val requestName:String?,
    var members: MutableList<Movie> = mutableListOf(),
    var expanded: Boolean = true //
)
