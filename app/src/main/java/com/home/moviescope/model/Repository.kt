package com.home.moviescope.model

class Repository:Repo {
    override fun getCategoryFromServer(): List<Category> {
        /**
         * дефолтный набор категорий
         * как то загрузить динамически пока не представляется возможным
         * либо прибито либо нужен механизм генерации запроса на список фильмов
         * что то вроде фильтра
         * но из API доступны сразу эти 4 категории на них и будем опираться
         */
        return listOf(
            Category("Now in theatres","now_playing"),
            Category("Popular","popular"),
            Category("Top rated","top_rated"),
            Category("Upcoming","upcoming")
        )
    }
}