package com.home.moviescope.repository

import com.home.moviescope.model.Category

interface Repo {
   fun getCategoryFromServer():List<Category>
}
