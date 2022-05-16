package com.mhamdy.data

import android.app.Application
import com.mhamdy.core.CoreIntegrationDsl


internal lateinit var domain: Application private set

@DslMarker
annotation class DataIntegrationDsl

@DataIntegrationDsl
@Suppress("FunctionName")
fun Data(integrate: AdaptersFactoryProvider.() -> Unit) {
    integrate(AdaptersFactoryProvider())
}

class AdaptersFactoryProvider {
    @DataIntegrationDsl
    val initialize by lazy { AdaptersFactory() }

    @CoreIntegrationDsl
    val and by lazy { initialize }
}


class AdaptersFactory {

    @DataIntegrationDsl
    infix fun with(obj: Any) {
        when (obj) {
            is Application -> domain = obj
        }

    }
}
