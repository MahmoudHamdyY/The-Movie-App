package com.mhamdy.core.credits

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

internal class CreditsUseCaseKtTest {

    @Test
    fun `get credit of 2 movies and return the most 5 popular actors`() {
        val creditRepo = mock(CreditRepo::class.java)
        val firstMovieCredit = Credits(1,
            listOf(
                Person(11, "p1", "Acting", 1.5f, null),
                Person(12, "p2", "Acting", 1.3f, null),
                Person(13, "p3", "Acting", 1.9f, null)
            ), listOf()
        )
        val secondMovieCredit = Credits(2,
            listOf(
                Person(21, "p7", "Acting", 1f, null),
                Person(22, "p8", "Acting", 0.8f, null),
                Person(23, "p9", "Acting", 1.95f, null)
            ), listOf()
        )
        val expectedActors = listOf(
            Person(23, "p9", "Acting", 1.95f, null),
            Person(13, "p3", "Acting", 1.9f, null),
            Person(11, "p1", "Acting", 1.5f, null),
            Person(12, "p2", "Acting", 1.3f, null),
            Person(21, "p7", "Acting", 1f, null)
        )

        val result = runBlocking {
            whenever(creditRepo.getCreditForMovie(1)) doReturn firstMovieCredit
            whenever(creditRepo.getCreditForMovie(2)) doReturn secondMovieCredit

            runCatching {
                getMoviesCredits(listOf(1, 2), creditRepo)
            }.getOrNull()
        }

        assert(result?.actors?.containsAll(expectedActors) == true && expectedActors.containsAll(result.actors))
    }

    @Test
    fun `get credit of 2 movies and return the most 5 popular directors`() {
        val creditRepo = mock(CreditRepo::class.java)
        val firstMovieCredit = Credits(1, listOf(),
            listOf(
                Person(14, "p4", "Directing", 1.2f, null),
                Person(15, "p5", "Editing", 1.1f, null),
                Person(16, "p6", "Production", 1.4f, null),
                Person(17, "p7", "Directing", 1.0f, null),
                Person(18, "p9", "Directing", 0.9f, null)
            )
        )
        val secondMovieCredit = Credits(2, listOf(),
            listOf(
                Person(24, "p10", "Directing", 1.9f, null),
                Person(25, "p11", "Editing", 2.1f, null),
                Person(26, "p12", "Directing", 2.3f, null),
                Person(27, "p12", "Directing", 2.1f, null)
            )
        )
        val expectedDirectors = listOf(
            Person(26, "p12", "Directing", 2.3f, null),
            Person(27, "p12", "Directing", 2.1f, null),
            Person(24, "p10", "Directing", 1.9f, null),
            Person(14, "p4", "Directing", 1.2f, null),
            Person(17, "p7", "Directing", 1.0f, null)
        )

        val result = runBlocking {
            whenever(creditRepo.getCreditForMovie(1)) doReturn firstMovieCredit
            whenever(creditRepo.getCreditForMovie(2)) doReturn secondMovieCredit

            runCatching {
                getMoviesCredits(listOf(1, 2), creditRepo)
            }.getOrNull()
        }

        assert(result?.directors?.containsAll(expectedDirectors) == true && expectedDirectors.containsAll(result.directors))
    }

    @Test
    fun `get credit of 2 movies and the one with lowest popularity not in the list`() {
        val creditRepo = mock(CreditRepo::class.java)
        val firstMovieCredit = Credits(1,
            listOf(
                Person(11, "p1", "Acting", 1.5f, null),
                Person(12, "p2", "Acting", 1.3f, null),
                Person(13, "p3", "Acting", 1.9f, null)
            ), listOf()
        )
        val secondMovieCredit = Credits(2,
            listOf(
                Person(21, "p7", "Acting", 1f, null),
                Person(22, "p8", "Acting", 0.8f, null),
                Person(23, "p9", "Acting", 1.95f, null)
            ), listOf()
        )
        val expectedActors = listOf(
            Person(23, "p9", "Acting", 1.95f, null),
            Person(13, "p3", "Acting", 1.9f, null),
            Person(11, "p1", "Acting", 1.5f, null),
            Person(12, "p2", "Acting", 1.3f, null),
            Person(21, "p7", "Acting", 1f, null)
        )

        val result = runBlocking {
            whenever(creditRepo.getCreditForMovie(1)) doReturn firstMovieCredit
            whenever(creditRepo.getCreditForMovie(2)) doReturn secondMovieCredit

            runCatching {
                getMoviesCredits(listOf(1, 2), creditRepo)
            }.getOrNull()
        }

        assert(result?.actors?.find { it.id == 22 } == null)
    }

}