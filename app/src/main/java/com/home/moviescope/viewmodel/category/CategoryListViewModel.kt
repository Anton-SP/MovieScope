package com.home.moviescope.viewmodel.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.model.Category

class CategoryListViewModel:ViewModel() {

    private val _categoryList = MutableLiveData<List<Category>>(emptyList())
    val categoryList:LiveData<List<Category>>
    get() = _categoryList

    init {
       loadTestData()
    }

    private fun loadTestData() {
        var categoryList = listOf(
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
      _categoryList.postValue(categoryList)
    }

}