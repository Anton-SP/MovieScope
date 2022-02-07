package com.home.moviescope.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.databinding.MovieItemBinding
import com.home.moviescope.databinding.MovieListBinding
import com.home.moviescope.model.Category

class CategoryAdapter(var categoryList: List<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: MovieListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Category) {
            binding.categoryTitle.text = result.name
            val movieAdapter:MovieAdapter =  MovieAdapter(result.members)
            binding.movieRv.layoutManager=LinearLayoutManager(binding.root.context,LinearLayoutManager.HORIZONTAL,false)
            binding.movieRv.adapter = movieAdapter
        }
    }

    /*
  inner class ViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
          fun bind(movie: Movie) {
              binding.movieGenre.text=movie.genre
          }
      }

  */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.bind(categoryList[position])
     }

    override fun getItemCount(): Int {
      return categoryList.size
    }

    fun addData(list: List<Category>) {
        categoryList = list
        notifyDataSetChanged()
    }
}