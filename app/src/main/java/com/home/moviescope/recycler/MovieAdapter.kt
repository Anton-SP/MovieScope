package com.home.moviescope.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.databinding.MovieItemBinding
import com.home.moviescope.model.Movie
import com.home.moviescope.viewmodel.movie.MovieViewModel

//Start
//адаптер для вложенного ресайклера с фильмами
class MovieAdapter(var movieList: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    interface onMovieItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    private lateinit var movieListener: onMovieItemClickListener

    fun setOnItemMovieClickListener(listener: onMovieItemClickListener) {
        this.movieListener = listener
    }

    inner class ViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movieGenre.text = movie.title

        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    movieListener.onItemClick(itemView, position)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        movieList?.get(position)?.let { holder.bind(it)}
    }

    override fun getItemCount(): Int {
        return movieList?.size!!
    }

}
