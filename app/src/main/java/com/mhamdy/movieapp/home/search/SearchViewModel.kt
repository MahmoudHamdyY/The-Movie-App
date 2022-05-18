package com.mhamdy.movieapp.home.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mhamdy.core.movies.*
import com.mhamdy.movieapp.helpers.SingleLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    val loading: SingleLiveData<Boolean> = SingleLiveData(),
    val cards: MutableLiveData<MutableList<MoviesCard>> = MutableLiveData(mutableListOf()),
    val error: SingleLiveData<Throwable> = SingleLiveData(),
    val popularMovies: suspend (p: Int) -> MoviesPage = { getPopularMovies(it) },
    val searchMovie: suspend (q: String, p: Int) -> MoviesPage = { q, p -> searchMovies(q, p) },
    val loadWatchListedIds: suspend () -> List<Int> = { getWatchListedMoviesIds() },
    val appendMovies: suspend MutableList<MoviesCard>.(moviesPage: MoviesPage) -> Unit = { append(it) },
    val coroutineIoDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private var currentPage = 0
    private var canLoadMore = true

    fun loadMovies(query: String = "", update: Boolean = false) {
        if (update) {
            cards.value?.clear()
            cards.postValue(cards.value)
            currentPage = 0
            canLoadMore = true
        }
        if (!canLoadMore)
            return
        if (query.isEmpty())
            loadPopularMovies()
        else
            loadSearchMovies(query)
    }

    fun updateWatchListed() {
        viewModelScope.launch(coroutineIoDispatcher) {
            loading.postValue(true)
            runCatching {
                loadWatchListedIds()
            }.onSuccess { ids ->
                loading.postValue(false)
                cards.postValue(cards.value?.onEach { movieCard ->
                    if (movieCard.movie != null)
                        movieCard.movie!!.watchListed = ids.contains(movieCard.movie?.id)
                })
            }
        }
    }

    private fun loadPopularMovies() {
        viewModelScope.launch(coroutineIoDispatcher) {
            loading.postValue(true)
            runCatching {
                popularMovies(currentPage + 1)
            }.onSuccess { moviePage ->
                runCatching {
                    cards.value?.appendMovies(moviePage)
                    cards.postValue(cards.value)
                    currentPage = moviePage.page
                    canLoadMore = (moviePage.page < moviePage.totalPages)
                }.onSuccess {
                    loading.postValue(false)
                }.onFailure {
                    loading.postValue(false)
                    error.postValue(it)
                }
            }.onFailure {
                loading.postValue(false)
                error.postValue(it)
            }
        }
    }

    private fun loadSearchMovies(query: String) {
        viewModelScope.launch(coroutineIoDispatcher) {
            loading.postValue(true)
            runCatching {
                searchMovie(query, currentPage + 1)
            }.onSuccess { moviePage ->
                runCatching {
                    cards.value?.appendMovies(moviePage)
                    cards.postValue(cards.value)
                    currentPage = moviePage.page
                    canLoadMore = (moviePage.page < moviePage.totalPages)
                }.onSuccess {
                    loading.postValue(false)
                }.onFailure {
                    loading.postValue(false)
                    error.postValue(it)
                }
            }.onFailure {
                loading.postValue(false)
                error.postValue(it)
            }
        }
    }


    class SearchViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>):
                T = when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel() as T
            else -> throw IllegalArgumentException("viewModel does not exist")
        }
    }

}