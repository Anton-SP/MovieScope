package com.home.moviescope.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.databinding.MovieItemBinding
import com.home.moviescope.model.Movie

open class MovieAdapter (
    var movieList: List<Movie>
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

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
     
        /*with(holder){
            with(movieList[position]){
                binding.movieGenre.text = this.genre
            }
        }*/
    }


    override fun getItemCount(): Int {
        return movieList.size
    }

   /*
    var movieList: List<Movie>
        ) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
// inner class ViewHolder(val binding: SingleItemBinding) : RecyclerView.ViewHolder(binding.root)
inner class ViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(movieList[position]){
                binding.movieGenre.text = this.genre
            }
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }*/

}