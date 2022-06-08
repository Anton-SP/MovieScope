package com.home.moviescope.view


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.home.moviescope.R
import com.home.moviescope.databinding.MainFragmentBinding
import com.home.moviescope.recycler.CategoryAdapter
import com.home.moviescope.utils.showSnackbar
import com.home.moviescope.view.details.CategoryDetailedFragment
import com.home.moviescope.viewmodel.AppState
import com.home.moviescope.viewmodel.MainViewModel
import com.home.moviescope.viewmodel.category.CategoryListViewModel
import com.home.moviescope.viewmodel.category.CategoryViewModel
import com.home.moviescope.viewmodel.movie.MovieRepositoryViewModel
import com.home.moviescope.viewmodel.movie.MovieViewModel

private const val IS_LANGUAGE_RU = "LANGUAGE_KEY"
private const val RU = "ru-RU"
private const val EN = "en-US"

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter

    private var isDataSetRU: Boolean = false

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
    private val movieRepositoryViewModel: MovieRepositoryViewModel by activityViewModels()

    private var reqLanguage: String = EN

    private lateinit var switch: SwitchCompat

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
        switch = (activity as MainActivity).findViewById<SwitchCompat>(R.id.switch_language)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        checkLanguageRequest()

        val observer = Observer<AppState> { renderData(it) }
        mainViewModel.getLiveData().observe(viewLifecycleOwner, observer)
        mainViewModel.getCategoryFromRemoteSource()
        binding.catalogList.layoutManager = layoutManager

        /**
         * обновление по свайпу
         */
        binding.swipeRefreshLayout.setOnRefreshListener {
            categoryAdapter?.let { it.notifyDataSetChanged() }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessInit -> {
                categoryListModel.setList(appState.categoryData)
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.mainView.visibility = View.VISIBLE
                Log.d("@@@", "APP SuccessInit ")
                adapterInit()

                /**
                 * подгружаем фильмы в наших категориях
                 * не самый красимвый способ прокидывать адаптер во вью модель
                 * чтобы обновить ресйклер но лучше пока не придумал
                 * и да архитектура получатеся оч связанной
                 */

                downloadData(appState)

                switch.setOnCheckedChangeListener { buttonView, isChecked ->
                    isDataSetRU = isChecked
                    saveLanguageSettings(isDataSetRU)
                    Log.d("isRU", isDataSetRU.toString())
                    downloadData(appState)
                    var manager = requireActivity().supportFragmentManager
                    manager.findFragmentByTag("MAIN")?.let {
                        Log.d("FRG", it.tag.toString())
                        manager.beginTransaction().detach(it).commit()
                        manager.beginTransaction().attach(it).commit()
                    }

                }


                view?.showSnackbar(getString(R.string.success_load_message))
            }
            is AppState.Loading -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
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
            categoryAdapter.notifyDataSetChanged()
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

    /**
     * работаем с SharedPreference
     * по локазизации нам говорили надо создават отдельные ресурсы или тип того
     * (еще не показывали а так для общего развития упоминули)
     * но цель научиться рабоать с SharedPreference: сохранить его и как то использовать
     * нагляднее чем смена языка при загрузке фильмов ничего не придумал.
     */

    private fun checkLanguageRequest() {
        activity?.let {
            if (it.getPreferences(Context.MODE_PRIVATE)
                    .getBoolean(IS_LANGUAGE_RU, false)
            ) {
                isDataSetRU = true
                reqLanguage = RU
                with(switch) {
                    /**
                     * а вот этого трюка раньше не знал: чтобы программно переключить switch
                     * нужно отвязать от него Listener
                     */
                    setOnCheckedChangeListener(null)
                    isChecked = true
                    setOnCheckedChangeListener { buttonView, isChecked ->
                    }
                }
            } else {
                with(switch) {
                    setOnCheckedChangeListener(null)
                    isChecked = false
                    setOnCheckedChangeListener { buttonView, isChecked ->
                    }
                }
                reqLanguage = EN
            }
            saveLanguageSettings(isDataSetRU)
        }
    }

    private fun saveLanguageSettings(dataSetRU: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_LANGUAGE_RU, dataSetRU)
                apply()
            }
        }
    }

    private fun downloadData(appState: AppState) {
        when (appState) {
            is AppState.SuccessInit -> {
                categoryAdapter?.let { movieRepositoryViewModel.categoryAdapter = categoryAdapter }

                movieRepositoryViewModel.getGenresFromServer(reqLanguage)
                movieRepositoryViewModel.setList(appState.categoryData)

                for (i in appState.categoryData.indices) {
                    Log.d("@@@", "loadMovies in category= " + 1)
                    movieRepositoryViewModel.getMovieFromRemoteSource(
                        categoryListModel.categoryList.value?.get(i)?.requestName,
                        reqLanguage,
                        1,
                        i
                    )
                }
            }
        }
    }

}