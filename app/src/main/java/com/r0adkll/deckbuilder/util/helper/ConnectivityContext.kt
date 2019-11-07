package com.r0adkll.deckbuilder.util.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.r0adkll.deckbuilder.util.extensions.systemService
import java.util.concurrent.atomic.AtomicBoolean

class ConnectivityContext(context: Context) : Connectivity, ConnectivityManager.NetworkCallback() {

    private val connectivityManager by systemService<ConnectivityManager>(context, Context.CONNECTIVITY_SERVICE)
    private val isConnected = AtomicBoolean(true)

    init {
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), this)
    }

    override fun isConnected(): Boolean {
        return isConnected.get()
    }

    override fun onLost(network: Network) {
        isConnected.set(false)
    }

    override fun onAvailable(network: Network) {
        isConnected.set(true)
    }
}
