package com.mhamdy.core.credits

interface CreditRepo {
    suspend fun getCreditForMovie(id: Int): Credits
}