package com.home.moviescope.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.home.moviescope.R
import com.home.moviescope.databinding.MainFragmentBinding
import com.home.moviescope.model.Category
import com.home.moviescope.recycler.CategoryAdapter
import com.home.moviescope.recycler.MovieAdapter
import com.home.moviescope.viewmodel.AppState
import com.home.moviescope.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryList: List<Category>


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        //setTestCategory()
        return binding.root

    }
    //для проверки recycler
    /*  private fun setTestCategory() {
          categoryList = listOf(
              Category("Category 1",setTestMovie()),
              Category("Category 2",setTestMovie()),
              Category("Category 3",setTestMovie()),
              Category("Category 4",setTestMovie()),
              Category("Category 5",setTestMovie()),
              Category("Category 6",setTestMovie()),
              Category("Category 7",setTestMovie()),
              Category("Category 8",setTestMovie())
          )
      }

      private fun setTestMovie(): List<Movie> {
          return listOf(
              Movie("movie 1", "Horror"),
              Movie("movie 2", "Comedy"),
              Movie("movie 3", "Action"),
              Movie("movie 4", "Bio"),
              Movie("movie 5", "Detective"),
              Movie("movie 6", "Drama"),
              Movie("movie 7", "Horror"),
              Movie("movie 8", "Action")
          )
      }
  */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.catalogList.layoutManager = layoutManager

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getCategoryFromRemoteSource()
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
        categoryAdapter.setOnItemClickListener(object :CategoryAdapter.onItemClickListener{
            override fun onItemClick(itemView: View?, position: Int) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CategoryDetailedFragment.newInstance())
                    .addToBackStack(null)
                    .commit()

            //  Toast.makeText(requireContext(),"click",Toast.LENGTH_SHORT).show()
            }
        })

      /*  movieAdapter.setOnItemMovieClickListener(object  : MovieAdapter.onMovieItemClickListener{
            override fun onItemClick(itemView: View?, position: Int) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container,MovieFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        })*/



    }



}