package com.home.moviescope.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.R
import com.home.moviescope.databinding.CategoryDetailedFragmentBinding
import com.home.moviescope.model.Category
import com.home.moviescope.recycler.MovieAdapter
import com.home.moviescope.viewmodel.category.CategoryViewModel
import com.home.moviescope.viewmodel.movie.MovieViewModel

class CategoryDetailedFragment : Fragment() {
    private var _binding: CategoryDetailedFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailedAdapter: MovieAdapter
    private val categoryModel: CategoryViewModel by activityViewModels<CategoryViewModel>()
    private val movieModel: MovieViewModel by activityViewModels<MovieViewModel>()

    companion object {
        const val CATEGORY_DETAIL: String = "CATEGORY_DETAIL"
        fun newInstance(): CategoryDetailedFragment {
            return CategoryDetailedFragment()//.apply { arguments = bundle }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CategoryDetailedFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryModel.category.observe(viewLifecycleOwner, Observer { category ->
            binding.categoryDetailTitle.text = category.name
            setData(category)
        })
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(
            requireContext(), 2, GridLayoutManager.VERTICAL, false
        )
        binding.movieRecycle.layoutManager = layoutManager
    }

    private fun setData(category: Category) {
        detailedAdapter = MovieAdapter(category.members)
        binding.movieRecycle.adapter = detailedAdapter
            .apply {
                setOnItemMovieClickListener(object : MovieAdapter.onMovieItemClickListener {
                    override fun onItemClick(itemView: View?, position: Int) {
                        movieModel.setMovie(detailedAdapter.movieList.get(position))
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, MovieFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    }
                })
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}