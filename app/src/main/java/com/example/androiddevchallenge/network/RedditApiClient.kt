package com.example.androiddevchallenge.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RedditApiClient {

    private val apiService: RedditService
    private val retrofitInstance: Retrofit

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(LoggingInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .build()

        retrofitInstance = Retrofit.Builder()
            .baseUrl(getBaseApiUrl())
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        apiService = retrofitInstance.create(RedditService::class.java)
    }

    private fun getBaseApiUrl(): String {
        return "https://www.reddit.com/"
    }

    fun getApiService(): RedditService {
        return apiService
    }

    fun getRetrofitInstance(): Retrofit {
        return retrofitInstance
    }

    companion object {
        private val TAG = RedditApiClient::class.java.simpleName
    }
}
