package com.home.moviescope.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.home.moviescope.R
import com.home.moviescope.databinding.TestDBFragmentBinding
import com.home.moviescope.recycler.TestDBAdapter
import com.home.moviescope.viewmodel.AppState

class TestDBFragment : Fragment() {

    private var _binding: TestDBFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TestDBViewModel by lazy {
        ViewModelProvider(this).get(TestDBViewModel::class.java)
    }
    private val adapter: TestDBAdapter by lazy { TestDBAdapter() }

    companion object {
        @JvmStatic
        fun newInstance() = TestDBFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TestDBFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.testDBFragmentRecyclerview.adapter = adapter
        viewModel.genresLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getAllGenres()
    }

    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.SuccessInitDB -> {
                Log.d("testdb", "we here")
                Log.d("testdb", appState.genresData.toString())

                binding.testDBFragmentRecyclerview.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                adapter.setData(appState.genresData)
            }
            is AppState.Loading->{
                binding.testDBFragmentRecyclerview.visibility = View.GONE
               binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}