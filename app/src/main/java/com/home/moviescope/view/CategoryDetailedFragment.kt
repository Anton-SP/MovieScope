package com.home.moviescope.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.R
import com.home.moviescope.databinding.CategoryDetailedFragmentBinding
import com.home.moviescope.model.Category
import com.home.moviescope.recycler.MovieAdapter

class CategoryDetailedFragment : Fragment() {
    private var _binding: CategoryDetailedFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailedAdapter: MovieAdapter

    companion object {

        const val CATEGORY_DETAIL: String = "CATEGORY_DETAIL"

        fun newInstance(bundle: Bundle): CategoryDetailedFragment {
            return CategoryDetailedFragment().also { it.arguments = bundle }
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
        val category = arguments?.getParcelable<Category>(CATEGORY_DETAIL)?.also {
            binding.categoryDetailTitle.text = it.name
            setData(it)
        }

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
                        var bundle =
                            Bundle().apply {
                                putParcelable(
                                    MovieFragment.MOVIE,
                                    detailedAdapter.movieList[position]
                                )
                            }

                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, MovieFragment.newInstance(bundle))
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