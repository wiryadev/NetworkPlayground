package com.wiryadev.networkplayground.connection

import android.content.Context
import android.net.NetworkRequest
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.wiryadev.networkplayground.connection.Connection.Lost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

fun ComponentActivity.flowConnectionObserver(
    networkRequest: NetworkRequest? = null
): Lazy<FlowConnectionObserver> = lazy {
    val connectionObserver = FlowConnectionObserver(
        baseContext,
        networkRequest ?: defaultNetworkRequest
    )
    lifecycle.addObserver(connectionObserver)
    connectionObserver
}

fun Fragment.flowConnectionObserver(
    networkRequest: NetworkRequest? = null
): Lazy<FlowConnectionObserver> = lazy {
    val connectionObserver = FlowConnectionObserver(
        requireContext(),
        networkRequest ?: defaultNetworkRequest
    )
    viewLifecycleOwner.lifecycle.addObserver(connectionObserver)
    connectionObserver
}

class FlowConnectionObserver(
    context: Context,
    networkRequest: NetworkRequest,
) : ConnectionObserverImpl(context, networkRequest) {

    private val _connection: MutableStateFlow<Connection> = MutableStateFlow(Lost)
    val connection: StateFlow<Connection> = _connection.asStateFlow()

    override fun onConnectionChanged(connection: Connection) {
        _connection.tryEmit(connection)
    }
}