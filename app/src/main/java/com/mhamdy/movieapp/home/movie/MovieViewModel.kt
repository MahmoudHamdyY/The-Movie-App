package com.mhamdy.movieapp.home.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mhamdy.core.credits.CreditView
import com.mhamdy.core.credits.getMoviesCredits
import com.mhamdy.core.movies.*
import com.mhamdy.movieapp.helpers.SingleLiveData
import kotlinx.coroutines.launch

class MovieViewModel(
    val loading: SingleLiveData<Boolean> = SingleLiveData(),
    val error: SingleLiveData<Throwable> = SingleLiveData(),
    val movie: MutableLiveData<Movie> = MutableLiveData(),
    val similarMovies: MutableLiveData<List<Movie>> = MutableLiveData(),
    val moviesCredits: MutableLiveData<CreditView> = MutableLiveData(),
    val findMovie: suspend (id: Int) -> Movie = { getMovie(it) },
    val findSimilarMovies: suspend (id: Int) -> List<Movie> = { getSimilarMovies(it) },
    val findMoviesCredits: suspend (ids: List<Int>) -> CreditView = { getMoviesCredits(it) },
    val setMovieToWatchListed: suspend (movie: Movie) -> Unit = { addMovieToWatchList(it) },
    val removeMovieFromWatchListed: suspend (movie: Movie) -> Unit = { removeMovieFromWatchList(it) }
) : ViewModel() {

    fun load(movieId: Int) {
        loadMovie(movieId)
        loadSimilarMovies(movieId)
    }

    fun addToWatchList() {
        viewModelScope.launch {
            loading.postValue(true)
            runCatching {
                setMovieToWatchListed(movie.value!!)
            }.onSuccess {
                loading.postValue(false)
                movie.postValue(movie.value!!.apply { watchListed = true })
            }.onFailure {
                loading.postValue(false)
                error.postValue(it)
            }
        }
    }

    fun removeWatchListed() {
        viewModelScope.launch {
            loading.postValue(true)
            runCatching {
                removeMovieFromWatchListed(movie.value!!)
            }.onSuccess {
                loading.postValue(false)
                movie.postValue(movie.value!!.apply { watchListed = false })
            }.onFailure {
                loading.postValue(false)
                error.postValue(it)
            }
        }
    }

    private fun loadMovie(id: Int) {
        viewModelScope.launch {
            loading.postValue(true)
            runCatching {
                findMovie(id)
            }.onSuccess {
                movie.postValue(it)
                loading.postValue(false)
            }.onFailure {
                error.postValue(it)
                loading.postValue(false)
            }
        }
    }

    private fun loadSimilarMovies(id: Int) {
        viewModelScope.launch {
            loading.value = true
            runCatching {
                findSimilarMovies(id)
            }.onSuccess {
                similarMovies.value = it
                loading.value = false
                loadMoviesCredits(it.map { movie -> movie.id })
            }.onFailure {
                error.value = it
                loading.value = false
            }
        }
    }

    private fun loadMoviesCredits(ids: List<Int>) {
        viewModelScope.launch {
            loading.value = true
            runCatching {
                findMoviesCredits(ids)
            }.onSuccess {
                moviesCredits.value = it
                loading.value = false
            }.onFailure {
                error.value = it
                loading.value = false
            }
        }
    }


    class MovieViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>):
                T = when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> MovieViewModel() as T
            else -> throw IllegalArgumentException("viewModel does not exist")
        }
    }
}