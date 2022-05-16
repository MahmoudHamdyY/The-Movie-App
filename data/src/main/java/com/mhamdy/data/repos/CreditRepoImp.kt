package com.mhamdy.data.repos

import com.mhamdy.core.CoreIntegrationDsl
import com.mhamdy.core.credits.CreditRepo
import com.mhamdy.core.credits.Credits
import com.mhamdy.data.network.Api
import com.mhamdy.data.network.getApiService

class CreditRepoImp @CoreIntegrationDsl constructor(
    private val api: Api = getApiService()
) : CreditRepo {

    override suspend fun getCreditForMovie(id: Int): Credits {
        return runCatching {
            api.getMovieCredits(id)
        }.getOrElse {
            throw Exception("Something Went Wrong", it)
        }
    }

}