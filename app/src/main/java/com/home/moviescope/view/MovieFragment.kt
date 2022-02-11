package com.home.moviescope.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.home.moviescope.databinding.MovieFragmentBinding
import com.home.moviescope.model.Movie

class MovieFragment : Fragment() {

    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val MOVIE: String = "MOVIE"

        @JvmStatic
        fun newInstance(bundle: Bundle): MovieFragment {
            val fragment = MovieFragment()
            fragment.arguments = bundle
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
        val movie = arguments?.getParcelable<Movie>(MOVIE)
        if (movie != null) {
            binding.movieGenre.text = movie.genre
            binding.movieTitle.text = movie.title

        }

    }


}