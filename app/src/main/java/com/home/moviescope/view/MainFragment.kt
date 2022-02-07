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
import com.home.moviescope.databinding.MainFragmentBinding
import com.home.moviescope.model.Category
import com.home.moviescope.model.Movie
import com.home.moviescope.recycler.CategoryAdapter
import com.home.moviescope.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

  //  private lateinit var movieAdapter: MovieAdapter
    private lateinit var categoryAdapter: CategoryAdapter
  //  private lateinit var movieList: List<Movie>
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
        setTestCategory()

       // setTestMovie()
        return binding.root

       // binding.catalogList.layoutManager

    }

    private fun setTestCategory() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.catalogList.layoutManager=layoutManager

     //   movieAdapter=MovieAdapter(movieList)
        categoryAdapter = CategoryAdapter(categoryList)

        binding.catalogList.adapter = categoryAdapter

    }

    private fun setTestMovie(): List<Movie> {
        var movieList = listOf(
            Movie("movie 1","Horror"),
            Movie("movie 2","Comedy"),
            Movie("movie 3","Action"),
            Movie("movie 4","Bio"),
            Movie("movie 5","Detective"),
            Movie("movie 6","Drama"),
            Movie("movie 7","Horror"),
            Movie("movie 8","Action")
        )
        return movieList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        val observer = Observer<Any> { renderData(it) }

        viewModel.getData().observe(viewLifecycleOwner, observer)


    }

    private fun renderData(appState: Any) {
        Toast.makeText(requireContext(), "rendered", Toast.LENGTH_SHORT).show()

    }

}