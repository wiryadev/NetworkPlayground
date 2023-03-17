package com.wiryadev.networkplayground.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.activity.ComponentActivity
import com.wiryadev.networkplayground.connection.Connection.Lost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ConnectionManager {
    fun register()
    fun unregister()
}

class ConnectionManagerImpl(
    private val connectivityManager: ConnectivityManager,
    private val networkRequest: NetworkRequest,
    private val networkCallback: ConnectivityManager.NetworkCallback
) : ConnectionManager {

    override fun register() {
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun unregister() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}