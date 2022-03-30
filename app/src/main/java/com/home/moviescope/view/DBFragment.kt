package com.home.moviescope.view


import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.google.android.material.navigation.NavigationView
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
        viewModel._filtredList.observe((viewLifecycleOwner), Observer {
            if (it.isNotEmpty()) {
                adapter.setData(it)
            } else
                Toast.makeText(requireContext(), "Filtred all Results", Toast.LENGTH_SHORT).show()
        })

        binding.filterButton.setOnClickListener { View ->
            startFilter()
        }

        /* binding.filterInput.setOnEditorActionListener { v, actionId, event ->
             return@setOnEditorActionListener when (actionId) {
                 EditorInfo.IME_ACTION_DONE -> {
                     binding.filterButton.performClick()
                     hideKeyboard(requireView())
                     true
                 }
                 else -> false
             }
         }*/

        binding.filterInput.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    startFilter()
                    return true
                }
                return false
            }
        })

    }

    private fun startFilter() {
        if (binding.filterInput.editableText != null) {

            viewModel.viewModelScope.launch {
                Log.d("FILT", binding.filterInput.editableText.toString())
                viewModel.getFilterList(binding.filterInput.editableText.toString())

            }
            hideKeyboard(requireView())
        }
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

    private fun hideKeyboard(view: View) {
        val manager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)

    }

   
}