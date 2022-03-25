package com.home.moviescope.repository.categoryRepo

import com.home.moviescope.model.Category

interface Repo {
   fun getCategoryFromServer():List<Category>
}
