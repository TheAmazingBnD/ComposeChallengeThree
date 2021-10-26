package com.example.androiddevchallenge.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class LoggingIntercepter : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val time1 = System.nanoTime()

        Log.i(
            TAG,
            String.format(
                "Sending request %s on %s%n%s",
                request.url(),
                chain.connection(),
                request.headers()
            )
        )

        val response = chain.proceed(request)

        val time2 = System.nanoTime()

        Log.i(
            TAG,
            String.format(
                "Received response for %s in %.1fms%n%s",
                response.request().url(),
                (time2 - time1) / 1e6,
                response.headers()
            )
        )

        return response
    }

    companion object {
        private val TAG = LoggingIntercepter::class.java.simpleName
    }
}