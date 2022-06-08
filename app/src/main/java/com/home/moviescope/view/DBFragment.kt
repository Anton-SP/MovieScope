package com.home.moviescope.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.home.moviescope.databinding.DBFragmentBinding
import com.home.moviescope.recycler.DBAdapter
import com.home.moviescope.viewmodel.AppState
import kotlinx.coroutines.launch

class DBFragment : Fragment() {

    private var _binding: DBFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DBViewModel by lazy {
        ViewModelProvider(this).get(DBViewModel::class.java)
    }
    private val adapter: DBAdapter by lazy { DBAdapter(viewModel) }

    companion object {
        @JvmStatic
        fun newInstance() = DBFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DBFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.DBFragmentRecyclerview.adapter = adapter
        viewModel.genresAndMoviesLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.viewModelScope.launch { viewModel.getAllData() }
    }

    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.SuccessInitDB -> {
                binding.DBFragmentRecyclerview.visibility = View.VISIBLE
                adapter.setData(appState.moviesData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}