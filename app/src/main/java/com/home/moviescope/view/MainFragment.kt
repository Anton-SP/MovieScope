package com.home.moviescope.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.home.moviescope.R
import com.home.moviescope.databinding.MainFragmentBinding
import com.home.moviescope.recycler.CategoryAdapter
import com.home.moviescope.utils.loadMovies
import com.home.moviescope.utils.showSnackbar
import com.home.moviescope.view.details.CategoryDetailedFragment
import com.home.moviescope.viewmodel.AppState
import com.home.moviescope.viewmodel.MainViewModel
import com.home.moviescope.viewmodel.category.CategoryListViewModel
import com.home.moviescope.viewmodel.category.CategoryViewModel
import com.home.moviescope.viewmodel.movie.MovieViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter

    /**
     * это вычитал из
     * https://developer.android.com/topic/libraries/architecture/viewmodel
     * Use the 'by activityViewModels()' Kotlin property delegate
     * from the fragment-ktx artifact
     *
     * и по сути все viewModel кроме main
     * можно объединить в какую-нибудь sharedViewModel
     * и туда проиписать лайв даты просто хотел попробовать поработать как говорили на уроке
     * для каждого элемента своя лайв дата
     */
    private val mainViewModel: MainViewModel by activityViewModels()
    private val movieModel: MovieViewModel by activityViewModels()
    private val categoryModel: CategoryViewModel by activityViewModels()
    private val categoryListModel: CategoryListViewModel by activityViewModels()

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
        }
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
        val observer = Observer<AppState> { renderData(it) }
        mainViewModel.getLiveData().observe(viewLifecycleOwner, observer)
        mainViewModel.getCategoryFromRemoteSource()
        binding.catalogList.layoutManager = layoutManager

        binding.swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.getCategoryFromRemoteSource()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                categoryListModel.setList(appState.categoryData)
                binding.loadingLayout.visibility = View.GONE
                binding.mainView.visibility = View.VISIBLE
                Log.d("@@@", "APP Success ")
                adapterInit()
                /**
                 * подгружаем фильмы в наших категориях
                 */
                for (i in appState.categoryData.indices) {
                    Log.d("@@@", "loadMovies in category= " + i)
                    loadMovies(appState.categoryData, i, categoryAdapter)
                }
                view?.showSnackbar(getString(R.string.success_message))
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainFragment, "ERROR LOADING DATA", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RELOAD") {
                        mainViewModel.getCategoryFromRemoteSource()
                    }
                    .show()
            }
        }
    }

    private fun adapterInit() {
        Log.d("@@@", "Set data in ")
        categoryListModel.categoryList.observe(viewLifecycleOwner, Observer { categoryList ->
            categoryAdapter = CategoryAdapter(categoryList, movieModel)
            binding.catalogList.adapter = categoryAdapter
        })
        /**
         * клик по категории чтбы открыть детальный обзор
         */
        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.onItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                categoryModel.setCategory(categoryAdapter.categoryList[position])
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.container, CategoryDetailedFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        })
    }
}