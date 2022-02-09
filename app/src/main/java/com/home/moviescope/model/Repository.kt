package com.home.moviescope.model

class Repository:Repo {
    override fun getCategoryFromServer(): List<Category> {
        //дефолтный набор категорий
        return listOf(
            Category("Category 1"),
            Category("Category 2"),
            Category("Category 3"),
            Category("Category 4"),
            Category("Category 5"),
            Category("Category 6"),
            Category("Category 7"),
            Category("Category 8"),
            Category("Category 9"),
            Category("Category 10")

        )
    }


}