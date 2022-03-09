package com.home.moviescope.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.home.moviescope.BuildConfig
import com.home.moviescope.R
import com.home.moviescope.databinding.MainFragmentBinding
import com.home.moviescope.model.Category
import com.home.moviescope.model.CategoryDTO
import com.home.moviescope.model.Movie
import com.home.moviescope.model.Results
import com.home.moviescope.recycler.CategoryAdapter
import com.home.moviescope.view.details.CategoryDetailedFragment
import com.home.moviescope.viewmodel.AppState
import com.home.moviescope.viewmodel.MainViewModel
import com.home.moviescope.viewmodel.category.CategoryListViewModel
import com.home.moviescope.viewmodel.category.CategoryViewModel
import com.home.moviescope.viewmodel.movie.MovieViewModel
import okhttp3.*
import java.io.IOException
import java.net.URL
import java.util.ArrayList

const val LOAD_INTENT_FILTER = "LOAD_INTENT_FILTER"
const val INTENT_EMPTY_EXTRA = "INTENT_EMPTY_EXTRA"
const val LOAD_RESULT_EXTRA = "LOAD_RESULT_EXTRA"
const val DATA_EMPTY_EXTRA = "DATA_EMPTY_EXTRA"
const val RESPONSE_EMPTY_EXTRA = "RESPONSE_EMPTY_EXTRA"
const val URL_MALFORMED_EXTRA = "URL_MALFORMED_EXTRA"
const val REQUEST_ERROR_EXTRA = "REQUEST_ERROR_EXTRA"
const val REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST_ERROR_MESSAGE_EXTRA"
const val RESPONSE_SUCCESS_EXTRA = "RESPONSE_SUCCESS_EXTRA"
const val RESULTS_DTO_EXTRA = "RESULTS_DTO_EXTRA"
const val ID_EXTRA = "ID_EXTRA"
const val MAIN_LINK = "https://api.themoviedb.org/3/movie/"
private const val PROCESS_ERROR = "Обработка ошибки"

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter

   /* private val loadResultReceiver: BroadcastReceiver = object :
        BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.getStringExtra(LOAD_RESULT_EXTRA)) {
                INTENT_EMPTY_EXTRA -> Log.d("@@@", PROCESS_ERROR)
                DATA_EMPTY_EXTRA -> Log.d("@@@", PROCESS_ERROR)
                RESPONSE_EMPTY_EXTRA -> Log.d("@@@", PROCESS_ERROR)
                REQUEST_ERROR_EXTRA -> Log.d("@@@", PROCESS_ERROR)
                REQUEST_ERROR_MESSAGE_EXTRA -> Log.d("@@@", PROCESS_ERROR)
                URL_MALFORMED_EXTRA -> Log.d("@@@", PROCESS_ERROR)
                RESPONSE_SUCCESS_EXTRA -> {
                    var categoryDTO =
                        intent.getParcelableArrayListExtra<Results>(RESULTS_DTO_EXTRA)
                            ?.let { CategoryDTO(it) }
                    var id = intent.getIntExtra(ID_EXTRA, -1)
                    if (id != -1) {
                        categoryListModel.categoryList.observe(
                            viewLifecycleOwner,
                            Observer { categoryList ->
                                if (categoryDTO != null) {
                                    loadDataToCategory(
                                        categoryDTO, categoryList[id]
                                    )
                                }
                            })
                    }
                }
            }
        }
    }*/

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
    private val mainViewModel: MainViewModel by activityViewModels<MainViewModel>()
    private val movieModel: MovieViewModel by activityViewModels<MovieViewModel>()
    private val categoryModel: CategoryViewModel by activityViewModels<CategoryViewModel>()
    private val categoryListModel: CategoryListViewModel by activityViewModels<CategoryListViewModel>()


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

    private fun loadMovies(category: Category, id: Int) {
        Log.d("@@@", "On start Https")
      //  binding.mainView.visibility = View.GONE
      //  binding.loadingLayout.visibility = View.VISIBLE
        val client = OkHttpClient()
        val builder: Request.Builder = Request.Builder()
        builder.url(
            MAIN_LINK +
                    "${category.requestName}?api_key=${BuildConfig.MOVIEDB_API_KEY}&language=ru-RU&page=1"
        )
        Log.d("@@@", "URL="+ MAIN_LINK+category.requestName+"?api_key="+BuildConfig.MOVIEDB_API_KEY+"&language=ru-RU&page=1")
        val request: Request = builder.build()
        val call:Call = client.newCall(request)
        call.enqueue(object : Callback{
            val handler:Handler = Handler()
            override fun onFailure(call: Call, e: IOException) {
               Log.d("@@@",PROCESS_ERROR+11)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val serverResponse: String? = response.body()?.string()
                Log.d("@@@",serverResponse!!)
                if (response.isSuccessful && serverResponse !=null) {
                    Log.d("@@@","CALL NEW RENDER DATA")
                    handler.post {renderDataCat(Gson().fromJson(serverResponse,CategoryDTO::class.java),id)
                        Log.d("@@@","CALL END")
                    }
                } else {
                    Log.d("@@@",PROCESS_ERROR+22)
                }
            }
        })
    }

    private fun renderDataCat(categoryDTO: CategoryDTO,id: Int) {
        Log.d("@@@@", "render DATA"+ id)

        categoryListModel.categoryList.observe(viewLifecycleOwner, Observer { categoryList ->
            Log.d("@@@@", "LOAD TO CAT Success")
            loadDataToCategory(
                categoryDTO, categoryList[id])
        })

        Log.d("@@@@", "render DATA CLOSE")
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
                view?.showSnackbar(getString(R.string.success_message))
                Log.d("@@@", "APP Success ")

                /**
                 * инициализируем адаптер для внешнего рейклера
                 */
                setData()
                /**
                 * подгружаем фильмы в наших категориях
                 */
                for (i in appState.categoryData.indices) {
                    Log.d("@@@", "loadMovies in category= " + i)
                    loadMovies(appState.categoryData[i], i)
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

    fun View.showSnackbar(
        text: String,
        length: Int = Snackbar.LENGTH_SHORT
    ) {
        Snackbar.make(this, text, length).show()
    }

    fun View.showSnackbarError(
        text: String,
        length: Int = Snackbar.LENGTH_SHORT
    ) {
        Snackbar.make(this, text, length).show()
    }


    private fun loadDataToCategory(categoryDTO: CategoryDTO, category: Category) {
        Log.d("@@@", "LOAD DATA TO CATEGORY= " + category.name)
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
                Log.d("@@@@",category.name)
                Log.d("@@@@",movie.title!!)
                category.members.add(movie)
            }
        }
        categoryAdapter.notifyDataSetChanged()
        Log.d("@@@","Adapter UPDATE")
    }
}