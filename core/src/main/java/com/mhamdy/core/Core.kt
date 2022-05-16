package com.mhamdy.core

import com.mhamdy.core.credits.CreditRepo
import com.mhamdy.core.movies.MoviesRepo

internal lateinit var moviesRepoImp: MoviesRepo private set
internal lateinit var creditRepoImp: CreditRepo private set

@DslMarker
annotation class CoreIntegrationDsl

@CoreIntegrationDsl
@Suppress("FunctionName")
fun Core(integrate: AdaptersFactoryProvider.() -> Unit) {
    integrate(AdaptersFactoryProvider())
}

class AdaptersFactoryProvider {
    @CoreIntegrationDsl
    val initialize by lazy { AdaptersFactory() }

    @CoreIntegrationDsl
    val and by lazy { initialize }
}

class AdaptersFactory {

    @CoreIntegrationDsl
    infix fun with(repo: Any) {
        when (repo) {
            is MoviesRepo -> moviesRepoImp = repo
            is CreditRepo -> creditRepoImp = repo
        }
    }
}