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
import com.home.moviescope.model.Category
import com.home.moviescope.model.CategoryDTO
import com.home.moviescope.model.Movie
import com.home.moviescope.recycler.CategoryAdapter
import com.home.moviescope.view.details.CategoryDetailedFragment
import com.home.moviescope.viewmodel.AppState
import com.home.moviescope.viewmodel.MainViewModel
import com.home.moviescope.viewmodel.category.CategoryListViewModel
import com.home.moviescope.viewmodel.category.CategoryViewModel
import com.home.moviescope.viewmodel.movie.MovieViewModel
import java.util.ArrayList


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
     * и туда проиписать лайв даты протсо хотел попробовать поработать какговорили на уроке
     * для каждого элемента своя лайв дата
     */
    private val mainViewModel: MainViewModel by activityViewModels<MainViewModel>()
    private val movieModel: MovieViewModel by activityViewModels<MovieViewModel>()
    private val categoryModel: CategoryViewModel by activityViewModels<CategoryViewModel>()
    private val categoryListModel: CategoryListViewModel by activityViewModels<CategoryListViewModel>()

    private val onLoaderListener: Loader.LoaderListener =
        object : Loader.LoaderListener {
            override fun onLoaded(categoryDTO: CategoryDTO, category: Category) {
                loadDataToCategory(categoryDTO, category)
            }

            override fun onFailed(throwable: Throwable) {
                Snackbar
                    .make(binding.mainFragment, "ERROR LOADING DATA", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RELOAD") {
                        mainViewModel.getCategoryFromRemoteSource()
                    }
                    .show()
            }
        }

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
        val observer = Observer<AppState> { renderData(it) }
        mainViewModel.getLiveData().observe(viewLifecycleOwner, observer)
        mainViewModel.getCategoryFromRemoteSource()
        binding.catalogList.layoutManager = layoutManager
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
                view?.showSnackbar(getString(R.string.success_message))
                setData()
                /**
                 * подгружаем фильмы в наших категориях
                 */
                for (category in appState.categoryData) {
                    val loader = Loader(onLoaderListener, category)
                    loader.loadCategory(category.requestName)
                }

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

    private fun setData() {
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

    fun View.showSnackbar(
        text: String,
        length: Int = Snackbar.LENGTH_SHORT
    ) {
        Snackbar.make(this, text, length).show()
    }

    private fun loadDataToCategory(categoryDTO: CategoryDTO, category: Category) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            /**
             * так как наши данные по жанрам это массив закодированых ID
             * (раскодируем позже покане придумал как)
             * то для надежности пробегаемся по всем полученным данным
             * и добавляем эти объекты по однному
             * затем создаем Фильм и добавляем к нашей категории
             */
            for (i in categoryDTO.results.indices) {
                var ids: ArrayList<Int> = arrayListOf()
                for (j in categoryDTO.results[i].genre_ids.indices) {
                    ids.add(categoryDTO.results[i].genre_ids[j])
                }
                var movie = Movie(
                    ids,
                    categoryDTO.results[i].title,
                    categoryDTO.results[i].overview
                )
                category.members.add(movie)
            }
        }
        categoryAdapter.notifyDataSetChanged()
    }

}