package com.home.moviescope.model

class Repository:Repo {
    override fun getCategoryFromServer(): List<Category> {
        /**
         * дефолтный набор категорий
         */
        return listOf(
            Category("Now in theatres ","now_playing"),
            Category("Popular","popular"),
            Category("Top rated","top_rated"),
            Category("Upcoming","upcoming")
        )
    }
}