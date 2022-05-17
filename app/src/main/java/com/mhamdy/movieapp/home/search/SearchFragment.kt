package com.mhamdy.movieapp.home.search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mhamdy.movieapp.R
import com.mhamdy.movieapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment(), SearchAdapter.MovieSelectListener {

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchAdapter
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initObservables()
        initListeners()
        viewModel.loadMovies()
    }

    private fun initData() {
        viewModel =
            ViewModelProvider(requireActivity(), SearchViewModel.SearchViewModelFactory()).get(
                SearchViewModel::class.java
            )
        adapter = SearchAdapter(this)
        binding.moviesRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.moviesRv.adapter = adapter
    }

    private fun initObservables() {
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.moviesRv.removeOnScrollListener(scrollListener)
                binding.loadingPb.visibility = View.VISIBLE
            } else {
                binding.moviesRv.addOnScrollListener(scrollListener)
                binding.loadingPb.visibility = View.GONE
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it?.message, Toast.LENGTH_LONG).show()
        }
        viewModel.cards.observe(viewLifecycleOwner) {
            if (it.isEmpty() && binding.searchBar.text.isNotEmpty()) {
                binding.noResultTv.text = getString(
                    R.string.oops_n_we_can_t_find_movies_with_title,
                    binding.searchBar.text
                )
                binding.noResultTv.visibility = View.VISIBLE
                binding.moviesRv.visibility = View.GONE
            } else if (it.isNotEmpty()) {
                adapter.submitList(it.toMutableList())
                binding.noResultTv.visibility = View.GONE
                binding.moviesRv.visibility = View.VISIBLE
                binding.clearSearchIv.visibility =
                    if (binding.searchBar.text.isNotEmpty())
                        View.VISIBLE
                    else
                        View.GONE
            }
        }
    }

    private fun initListeners() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (lastVisibleItem == (recyclerView.adapter?.itemCount ?: 0) - 1)
                    viewModel.loadMovies(binding.searchBar.text.trim().toString())
            }
        }
        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                (requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    view?.windowToken,
                    0
                )
                binding.searchBar.clearFocus()
                viewModel.loadMovies(binding.searchBar.text.trim().toString(), true)
                true
            } else
                false
        }
        binding.clearSearchIv.setOnClickListener {
            binding.searchBar.text.clear()
            viewModel.loadMovies(update = true)
        }
        binding.searchBar.showSoftInputOnFocus = true
    }

    override fun onMovieSelected(movieId: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionNavigationSearchToNavigationMovie(
                movieId
            )
        )
    }

}