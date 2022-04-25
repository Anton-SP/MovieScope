package com.home.moviescope.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import coil.load
import com.home.moviescope.databinding.MovieFragmentBinding
import com.home.moviescope.utils.showSnackbar
import com.home.moviescope.viewmodel.movie.MovieRepositoryViewModel
import com.home.moviescope.viewmodel.movie.MovieViewModel
import kotlinx.coroutines.launch

class MovieFragment : Fragment() {

    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!
    private val movieModel: MovieViewModel by activityViewModels<MovieViewModel>()
    private val movieRepositoryViewModel: MovieRepositoryViewModel by activityViewModels()

    companion object {
        const val MOVIE: String = "MOVIE"

        @JvmStatic
        fun newInstance(): MovieFragment {
            val fragment = MovieFragment()
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
            binding.movieGenre.text = movie.genreString
            binding.movieTitle.text = movie.title
            binding.movieDescription.text =
                StringBuilder().append("\t").append(movie.overview).toString()
            binding.moviePoster.load(movie.poster_path)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }

}