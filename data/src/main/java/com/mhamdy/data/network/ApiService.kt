package com.mhamdy.data.network

import com.mhamdy.data.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private val service: Api by lazy {
    Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(getHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)
}

fun getApiService() = service

private fun getHttpClient(): OkHttpClient {
    val logInterceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG)
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
    else
        logInterceptor.level = HttpLoggingInterceptor.Level.NONE

    return OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addInterceptor(logInterceptor)
        .build()
}

private val authInterceptor: Interceptor = Interceptor { chain ->
    var request: Request = chain.request()
    request = request.newBuilder()
        .url(
            request.url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()
        )
        .build()

    chain.proceed(request)
}