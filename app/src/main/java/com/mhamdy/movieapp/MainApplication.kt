package com.mhamdy.movieapp

import android.app.Application
import com.mhamdy.core.Core
import com.mhamdy.data.Data
import com.mhamdy.data.repos.CreditRepoImp
import com.mhamdy.data.repos.MoviesRepoImp

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Data {
            initialize with this@MainApplication
        }

        Core {
            initialize with CreditRepoImp()
            and with MoviesRepoImp()
        }

    }
}