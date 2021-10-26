package com.example.androiddevchallenge.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val tokenRequest = request.newBuilder().build()
        return chain.proceed(tokenRequest)
    }

    companion object {
        private val TAG = HeaderInterceptor::class.java.simpleName
    }
}