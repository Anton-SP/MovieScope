package com.home.moviescope.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.R
import com.home.moviescope.databinding.CategoryListBinding
import com.home.moviescope.model.Category
import com.home.moviescope.view.details.MovieFragment
import com.home.moviescope.viewmodel.movie.MovieViewModel

/**
 * адаптер для внешнего ресайклера (категорий) включает в себя адаптер для вложенного горизонтального ресайклера (фильмы)
 *
 * не знаю как тут получить досутп к viewmodel передал как параметр но это не правильно т.к.
 * адаптер находтися на уровне VIEW и приписываь к нему ссылку на viewModel не очень корректно.
 *
 */
//
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
            binding.movieRv.layoutManager =
                LinearLayoutManager(
                    binding.root.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            movieAdapter = MovieAdapter(category.members)
            binding.movieRv.adapter = movieAdapter

            movieAdapter.setOnItemMovieClickListener(object :
                MovieAdapter.onMovieItemClickListener {
                override fun onItemClick(itemView: View?, position: Int) {

                    movieAdapter.movieList = category.members
                    movieModel.setMovie(movieAdapter.movieList[position])

                    val activity = itemView?.context as AppCompatActivity
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MovieFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                }
            })
            Log.d("BINDCAT", category.name)
            Log.d("BINDCAT", movieModel.movie.value?.title.toString())

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