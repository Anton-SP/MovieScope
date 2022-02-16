package com.home.moviescope.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.moviescope.R
import com.home.moviescope.databinding.FragmentCategoryDetailedBinding
import com.home.moviescope.model.Category
import com.home.moviescope.model.Movie
import com.home.moviescope.recycler.DetailedAdapter
import com.home.moviescope.viewmodel.MainViewModel

//ПОКА КАК ЗАГЛУШКА. бужет отображать грид из фильмов в выбранной категории
class CategoryDetailedFragment : Fragment() {
    private var _binding: FragmentCategoryDetailedBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailedAdapter: DetailedAdapter
    private lateinit var categoryDetailed: Category

    companion object {

        fun newInstance() = CategoryDetailedFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(
            requireContext(),2, GridLayoutManager.VERTICAL, false
        )
        binding.movieRecycle.layoutManager = layoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
