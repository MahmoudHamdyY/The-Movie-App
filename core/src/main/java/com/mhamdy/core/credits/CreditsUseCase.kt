package com.mhamdy.core.credits

import com.mhamdy.core.creditRepoImp

suspend fun getMoviesCredits(ids: List<Int>, creditRepo: CreditRepo = creditRepoImp): CreditView {
    val actors: MutableList<Person> = mutableListOf()
    val directors: MutableList<Person> = mutableListOf()

    ids.forEach { id ->
        val credit = creditRepo.getCreditForMovie(id)
        actors.addAll(credit.cast.filter { it.department == "Acting" })
        directors.addAll(credit.crew.filter { it.department == "Directing" })
    }

    actors.sortByDescending { it.popularity }
    directors.sortByDescending { it.popularity }

    return CreditView(actors.take(5), directors.take(5))
}