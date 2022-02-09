package com.home.moviescope.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.databinding.MovieItemBinding
import com.home.moviescope.model.Movie
//адаптер для вложенного ресайклера с фильмами
open class MovieAdapter (
    var movieList: List<Movie>
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MovieItemBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun bind(movie: Movie) {
            binding.movieGenre.text=movie.genre
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding,movieListener)
    }

    fun bind(result: Movie) {
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    private  lateinit var movieListener: onItemClickListener

    interface onItemClickListener {

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        movieListener = listener
    }


}