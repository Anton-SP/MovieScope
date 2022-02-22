package com.home.moviescope.viewmodel.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.model.Category

class CategoryViewModel : ViewModel() {
    private val _category = MutableLiveData<Category>()
    val category: LiveData<Category>
        get() = _category

    fun setCategory(category: Category) {
        _category.value = category
    }
}
