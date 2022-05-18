package com.mhamdy.movieapp.home.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mhamdy.core.movies.MoviesCard
import com.mhamdy.movieapp.R
import com.mhamdy.movieapp.databinding.HeaderCardItemBinding
import com.mhamdy.movieapp.databinding.MovieCardItemBinding

class SearchAdapter(
    private val callback: MovieSelectListener
) : ListAdapter<MoviesCard, SearchAdapter.SearchViewHolder>(movieItemsDiffUtil()) {

    companion object {
        const val MOVIE_CARD_ITEM: Int = 0x1011
        const val HEADER_CARD_ITEM: Int = 0x1022

        fun movieItemsDiffUtil() = object : DiffUtil.ItemCallback<MoviesCard>() {
            override fun areItemsTheSame(oldItem: MoviesCard, newItem: MoviesCard): Boolean {
                return if(oldItem.movie != null){
                    if(newItem.movie != null)
                        oldItem.movie!!.id == newItem.movie!!.id
                    else
                        false
                }else{
                    if(oldItem.header != null)
                        newItem.header == oldItem.header
                    else
                        false
                }
            }

            override fun areContentsTheSame(oldItem: MoviesCard, newItem: MoviesCard): Boolean {
                return if (oldItem.movie != null) {
                    if(newItem.movie == null)
                        return false
                    val oldMovie = oldItem.movie!!
                    val newMovie = newItem.movie!!
                    (oldMovie.id == newMovie.id
                            && oldMovie.title == newMovie.title
                            && oldMovie.overview == newMovie.overview
                            && oldMovie.posterPath == newMovie.posterPath)
                } else
                    oldItem.header == newItem.header
            }

        }
    }

    override fun getItemViewType(position: Int): Int =
        if (getItem(position).movie != null)
            MOVIE_CARD_ITEM
        else
            HEADER_CARD_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        if (viewType == MOVIE_CARD_ITEM) {
            val binding =
                MovieCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieViewHolder(binding)
        } else {
            val binding =
                HeaderCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)
        }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position), callback)
        val movie = getItem(position)
        if (getItemViewType(position) == MOVIE_CARD_ITEM)
            holder.itemView.setOnClickListener {
                callback.onMovieSelected(movie.movie!!.id)
            }
        else
            holder.itemView.setOnClickListener(null)

    }

    inner class MovieViewHolder(private val binding: MovieCardItemBinding) :
        SearchViewHolder(binding.root) {
        override fun bind(moviesCard: MoviesCard, callback: MovieSelectListener) {
            val movie = moviesCard.movie!!
            with(binding) {
                titleTv.text = movie.title
                overviewTv.text = movie.overview
                watchlistedIv.visibility =
                    if (movie.watchListed == true)
                        View.VISIBLE
                    else
                        View.GONE
                Glide.with(root.context).load(movie.posterUrl()).fitCenter()
                    .placeholder(R.drawable.image_empty)
                    .error(R.drawable.image_empty)
                    .into(movieImg)
            }
        }
    }

    inner class HeaderViewHolder(private val binding: HeaderCardItemBinding) :
        SearchViewHolder(binding.root) {
        override fun bind(moviesCard: MoviesCard, callback: MovieSelectListener) {
            binding.headerTv.text = moviesCard.header
        }
    }

    abstract inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(moviesCard: MoviesCard, callback: MovieSelectListener)
    }

    interface MovieSelectListener {
        fun onMovieSelected(movieId: Int)
    }
}