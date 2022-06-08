package com.home.moviescope.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.home.moviescope.databinding.MovieFragmentBinding
import com.home.moviescope.model.Movie
import com.home.moviescope.viewmodel.category.CategoryViewModel
import com.home.moviescope.viewmodel.movie.MovieViewModel

class MovieFragment : Fragment() {

    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!
    private val movieModel: MovieViewModel by activityViewModels<MovieViewModel>()

    companion object {
        const val MOVIE: String = "MOVIE"

        @JvmStatic
        fun newInstance(): MovieFragment {
            val fragment = MovieFragment()//.apply { arguments = bundle }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieModel.movie.observe(viewLifecycleOwner, Observer { movie ->
            binding.movieGenre.text = movie.genre
            binding.movieTitle.text = movie.title
        })
       /* val movie = arguments?.getParcelable<Movie>(MOVIE)?.also {
            binding.movieGenre.text = it.genre
            binding.movieTitle.text = it.title
        } ?: throw IllegalArgumentException("Wrong data")*/
    }

}