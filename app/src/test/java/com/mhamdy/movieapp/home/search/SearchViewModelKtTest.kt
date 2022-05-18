package com.mhamdy.movieapp.home.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mhamdy.core.movies.Movie
import com.mhamdy.core.movies.MoviesCard
import com.mhamdy.core.movies.MoviesPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelKtTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun destroy() {
        Dispatchers.resetMain()
    }

    @Mock
    lateinit var observer: Observer<Any>

    @Test
    fun `load movies with empty query returns popular movies`() {
        val cards = MutableLiveData<MutableList<MoviesCard>>(mutableListOf())
        val movie = Movie(
            1, "harry potter", "2002", 1000000,
            "released", "overview", "url", "url",
            "tag", true
        )
        val popularMovies = MoviesPage(
            1, listOf(
                movie
            ), 10
        )
        val searchViewModel = SearchViewModel(
            cards = cards,
            popularMovies = { popularMovies },
            appendMovies = {
                it.movies.forEach { m ->
                    add(MoviesCard(m, null, 1, true))
                }
            },
            coroutineIoDispatcher = Dispatchers.Unconfined
        )
        cards.observeForever(observer)

        with(searchViewModel) {
            loadMovies()
        }

        verify(observer, times(2)).onChanged(
            mutableListOf(MoviesCard(movie, null, 1, true))
        )
    }

}