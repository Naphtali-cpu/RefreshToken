package com.mawinda.refresh_token_sample_app.network.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectionInterceptor(context: Context) : Interceptor {

    private val applicationContext by lazy {
        context.applicationContext
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (connectionIsOn()) {
            chain.proceed(chain.request())
        } else {
            throw IOException("Check network Connection")
        }
    }

    private fun connectionIsOn(): Boolean {
        val connectionManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectionManager.activeNetwork
        val connectionCapability = connectionManager.getNetworkCapabilities(network)
        return connectionCapability != null && connectionCapability.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_TRUSTED
        )
    }
}