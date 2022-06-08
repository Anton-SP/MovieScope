package com.home.moviescope.viewmodel.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.moviescope.model.Category

/**
 * это к слову о том что надо бы использовать livedata в адаптерах но еще не победил
 * к слову это еще не было целью дз)
 */
class CategoryListViewModel:ViewModel() {

    private val _categoryList = MutableLiveData<List<Category>>(emptyList())
    val categoryList:LiveData<List<Category>>
    get() = _categoryList

    fun setList(categoryList:List<Category>){
        _categoryList.value=categoryList
    }
}