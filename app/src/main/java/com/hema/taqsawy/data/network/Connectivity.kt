package com.hema.taqsawy.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


class Connectivity() {

    companion object {

         fun isOnline(context: Context): Boolean {
            val appContext = context.applicationContext
            val connectivityManager =
                appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            capabilities.also {
                if (it != null) {
                    if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                        return true
                    else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    }
                }
            }
            return false
        }
    }
}