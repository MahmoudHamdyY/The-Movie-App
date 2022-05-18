package com.mhamdy.movieapp.home.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mhamdy.core.credits.CreditView
import com.mhamdy.core.movies.Movie
import com.mhamdy.movieapp.R
import com.mhamdy.movieapp.databinding.FragmentMovieBinding

class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var viewModel: MovieViewModel
    private val args: MovieFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initObservables()
        initListeners()
    }

    override fun onResume() {
        if (::viewModel.isInitialized)
            viewModel.load(args.movieId)
        super.onResume()
    }

    private fun initData() {
        viewModel =
            ViewModelProvider(
                requireActivity(),
                MovieViewModel.MovieViewModelFactory()
            ).get(MovieViewModel::class.java)
        viewModel.load(args.movieId)
        binding.similarMovies.similarMoviesRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.casts.popularActorsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.casts.popularDirectorsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initObservables() {
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.loadingPb.visibility = View.VISIBLE
                binding.movieDetails.watchlistedIv.isEnabled = false
            } else {
                binding.loadingPb.visibility = View.GONE
                binding.movieDetails.watchlistedIv.isEnabled = true
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it?.message, Toast.LENGTH_LONG).show()
        }
        viewModel.movie.observe(viewLifecycleOwner) {
            bindMovieView(it)
        }
        viewModel.similarMovies.observe(viewLifecycleOwner) {
            bindSimilarMovieView(it)
        }
        viewModel.moviesCredits.observe(viewLifecycleOwner) {
            bindMovieCreditsView(it)
        }
    }

    private fun initListeners() {
        binding.movieDetails.watchlistedIv.setOnClickListener {
            if (viewModel.movie.value?.watchListed == true)
                viewModel.removeWatchListed()
            else
                viewModel.addToWatchList()
        }
    }

    private fun bindMovieView(movie: Movie) {
        binding.movieDetails.root.visibility = View.VISIBLE
        Glide.with(requireContext()).load(movie.backdropUrl()).fitCenter()
            .placeholder(R.drawable.image_empty)
            .error(R.drawable.image_empty)
            .into(binding.movieDetails.backdropImg)
        Glide.with(requireContext()).load(movie.posterUrl()).fitCenter()
            .placeholder(R.drawable.image_empty)
            .error(R.drawable.image_empty)
            .into(binding.movieDetails.posterImg)
        binding.movieDetails.overviewTv.text = movie.overview
        binding.movieDetails.titleTv.text = movie.title
        binding.movieDetails.statuesTv.text = movie.status
        binding.movieDetails.releaseDateTv.text = movie.releaseDate
        binding.movieDetails.taglineTv.text = movie.tagline
        binding.movieDetails.revenueTv.text = getString(R.string.revenue_s, movie.revenue)
        if (movie.watchListed == true)
            binding.movieDetails.watchlistedIv.setImageResource(R.drawable.watchlisted)
        else
            binding.movieDetails.watchlistedIv.setImageResource(R.drawable.add_to_watchlist)
    }

    private fun bindSimilarMovieView(movies: List<Movie>) {
        binding.similarMovies.root.visibility = View.VISIBLE
        val similarMoviesAdapter = ImageTitleCardAdapter()
        binding.similarMovies.similarMoviesRv.adapter = similarMoviesAdapter
        similarMoviesAdapter.submitList(movies.map {
            ImageTitleCardAdapter.ImageNameItem(
                it.posterUrl(),
                it.title
            )
        })
    }

    private fun bindMovieCreditsView(creditView: CreditView) {
        binding.casts.root.visibility = View.VISIBLE
        val popularActorsAdapter = ImageTitleCardAdapter()
        binding.casts.popularActorsRv.adapter = popularActorsAdapter
        popularActorsAdapter.submitList(creditView.actors.map {
            ImageTitleCardAdapter.ImageNameItem(
                it.profileImgUrl(),
                it.name
            )
        })
        val popularDirectorsAdapter = ImageTitleCardAdapter()
        binding.casts.popularDirectorsRv.adapter = popularDirectorsAdapter
        popularDirectorsAdapter.submitList(creditView.directors.map {
            ImageTitleCardAdapter.ImageNameItem(
                it.profileImgUrl(),
                it.name
            )
        })
    }
}