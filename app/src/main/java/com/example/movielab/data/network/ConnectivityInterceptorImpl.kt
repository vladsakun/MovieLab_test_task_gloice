package com.example.movielab.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.movielab.data.exceptions.NoConnectivityException
import com.example.movielab.isOnline
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptorImpl(
    context: Context
) : ConnectivityInterceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline(this.appContext))
            throw NoConnectivityException()
        return chain.proceed(chain.request())
    }

}