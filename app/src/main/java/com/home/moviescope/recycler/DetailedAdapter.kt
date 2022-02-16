package com.home.moviescope.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.databinding.MovieItemBinding
import com.home.moviescope.model.Movie
//адаптер для просмотра фильмов из 1 категории
open class DetailedAdapter (
    var movieList: List<Movie>
) : RecyclerView.Adapter<DetailedAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieGenre.text=movie.genre
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    fun bind(result: Movie) {
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

}

