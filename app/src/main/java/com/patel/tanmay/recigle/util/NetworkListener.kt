package com.patel.tanmay.recigle.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkListener : ConnectivityManager.NetworkCallback(){
    private val isNetworkAvaliable = MutableStateFlow(false)

    fun checkNetworkAvailability(context : Context) : MutableStateFlow<Boolean> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false

        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            networkCapability?.let {
                if(networkCapability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                {
                    isConnected = true
                    return@forEach
                }
            }

        }

        isNetworkAvaliable.value = isConnected

        return isNetworkAvaliable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvaliable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvaliable.value = false
    }
}