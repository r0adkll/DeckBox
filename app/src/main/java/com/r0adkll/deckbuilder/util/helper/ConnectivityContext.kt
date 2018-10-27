package com.r0adkll.deckbuilder.util.helper

import android.content.Context
import android.net.ConnectivityManager
import com.r0adkll.deckbuilder.util.extensions.systemService


class ConnectivityContext(context: Context): Connectivity {

    private val connectivityManager by systemService<ConnectivityManager>(context, Context.CONNECTIVITY_SERVICE)


    override fun isConnected(): Boolean {
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }
}