package com.example.footballfanapp.data.network

import com.example.footballfanapp.util.Constants.Companion.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("X-Auth-Token", API_KEY)
            .build()
        return chain.proceed(request)
    }

}