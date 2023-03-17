package com.wiryadev.networkplayground.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.wiryadev.networkplayground.connection.Connection.Available
import com.wiryadev.networkplayground.connection.Connection.Lost

sealed class Connection {
    data class Available(val isMetered: Boolean) : Connection()
    object Lost : Connection()
}

interface ConnectionObserver {
    fun onConnectionChanged(connection: Connection)
}

abstract class ConnectionObserverImpl(
    context: Context,
    networkRequest: NetworkRequest,
) : DefaultLifecycleObserver, ConnectionObserver {

    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val isMetered = !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            onConnectionChanged(Available(isMetered))
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            onConnectionChanged(Lost)
        }
    }

    private val mConnectionManager: ConnectionManager = ConnectionManagerImpl(connectivityManager, networkRequest, networkCallback)

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        mConnectionManager.register()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        mConnectionManager.unregister()
    }
}

internal val defaultNetworkRequest = NetworkRequest.Builder()
    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
    .build()

fun Connection.asString() = when (this) {
    is Lost -> "Disconnected"
    is Available -> if (isMetered) "Metered Connection" else "Unmetered Connection"
}