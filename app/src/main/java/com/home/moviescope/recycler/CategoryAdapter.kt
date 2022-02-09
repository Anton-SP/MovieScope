package com.home.moviescope.recycler

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.databinding.MovieListBinding
import com.home.moviescope.model.Category

import com.home.moviescope.view.MovieFragment
//адаптер для внешнего ресайклера (категорий) включает в себя адаптер для вложенного горизонтального ресайклера (фильмы)
class CategoryAdapter(var categoryList: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MovieListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Category) {
            binding.categoryTitle.text = result.name
            val movieAdapter = MovieAdapter(result.members)
            binding.movieRv.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.movieRv.adapter = movieAdapter

            movieAdapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {


                    //тут немного запутлся с обработкой нажатия на фильме
                    //и надо бы обработчик нажатия на категорию чтобы раскрыть фильмы категории
                    //наверно проще сделать кнопку рядом с названием категории по нажатию на которой будет открываться детальный обзор категории.
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

   /* fun addData(list: List<Category>) {
        categoryList = list
        notifyDataSetChanged()
    }*/
}