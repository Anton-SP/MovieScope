package com.home.moviescope.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.home.moviescope.databinding.DBItemBinding
import com.home.moviescope.model.Movie
import com.home.moviescope.view.DBViewModel
import kotlinx.coroutines.launch

/**
 * адаптер дляработы со списком из бд который предстаяет собой watch list
 */
class DBAdapter(val viewModel: DBViewModel) : RecyclerView.Adapter<DBAdapter.RecyclerItemViewHolder>() {
    private var data: MutableList<Movie> = mutableListOf()

    fun setData(data: MutableList<Movie>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class RecyclerItemViewHolder(val binding: DBItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                with(binding) {
                    movieTitle.text = movie.title
                    movieGenre.text = movie.genreString
                    movieDescription.text =
                        StringBuilder().append("\t").append(movie.overview).toString()
                    moviePoster.load(movie.poster_path)

                    removeFromWatchList.setOnClickListener { itemView->
                        viewModel.viewModelScope.launch {
                            viewModel.removeMovieFromDB(movie)
                            this@DBAdapter.notifyItemRemoved(layoutPosition)}
                    }
                }

            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding = DBItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecyclerItemViewHolder(binding)
    }

}