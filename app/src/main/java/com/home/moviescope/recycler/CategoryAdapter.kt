package com.home.moviescope.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.R
import com.home.moviescope.databinding.CategoryListBinding
import com.home.moviescope.model.Category
import com.home.moviescope.view.MovieFragment
import com.home.moviescope.viewmodel.movie.MovieViewModel

/**
 * адаптер для внешнего ресайклера (категорий) включает в себя адаптер для вложенного горизонтального ресайклера (фильмы)
 */
class CategoryAdapter(var categoryList: List<Category>, val movieModel: MovieViewModel) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    interface onItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    private lateinit var listener: onItemClickListener
    private lateinit var movieAdapter: MovieAdapter

    fun setOnItemClickListener(listener: onItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(private val binding: CategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var allButton = binding.categoryAll.apply {
            setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position)
                }
            }
        }

        fun bind(category: Category) {
            binding.categoryTitle.text = category.name
            movieAdapter = MovieAdapter(category.members)
            binding.movieRv.layoutManager =
                LinearLayoutManager(
                    binding.root.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            binding.movieRv.adapter = movieAdapter
            movieAdapter.setOnItemMovieClickListener(object :
                MovieAdapter.onMovieItemClickListener {
                override fun onItemClick(itemView: View?, position: Int) {
                    movieModel.setMovie(movieAdapter.movieList?.get(position))
                    //вот тут не знаю хорош ли это. контекст выдавать за активити
                    val activity = itemView?.context as AppCompatActivity
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MovieFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}