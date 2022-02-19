package com.home.moviescope.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.home.moviescope.R
import com.home.moviescope.databinding.MainFragmentBinding
import com.home.moviescope.model.Category
import com.home.moviescope.recycler.CategoryAdapter
import com.home.moviescope.viewmodel.AppState
import com.home.moviescope.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryList: List<Category>

    //    private lateinit var categoryAll: TextView
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getCategoryFromRemoteSource()
        binding.catalogList.layoutManager = layoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val categoryData = appState.categoryData
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.mainFragment, "Success", Snackbar.LENGTH_LONG).show()
                //load test data
                setData(categoryData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainFragment, "ERROR LOADING DATA", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RELOAD") {
                        viewModel.getCategoryFromRemoteSource()
                    }
                    .show()
            }
        }
    }

    private fun setData(categoryData: List<Category>) {
        categoryList = categoryData
        categoryAdapter = CategoryAdapter(categoryList)
        binding.catalogList.adapter = categoryAdapter
        //клик по категории чтбы открыть детайльный обзор
        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.onItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                var bundle = Bundle()
                bundle.putParcelable(
                    CategoryDetailedFragment.CATEGORY_DETAIL,
                    categoryAdapter.categoryList[position]
                )
                requireActivity().supportFragmentManager?.beginTransaction()
                    .add(R.id.container, CategoryDetailedFragment.newInstance(bundle))
                    .addToBackStack(null)
                    .commit()
            }
        })
    }
}