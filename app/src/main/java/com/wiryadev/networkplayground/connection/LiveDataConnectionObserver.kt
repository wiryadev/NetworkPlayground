package com.wiryadev.networkplayground.connection

import android.content.Context
import android.net.NetworkRequest
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wiryadev.networkplayground.connection.Connection.Lost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

fun ComponentActivity.livedataConnectionObserver(
    networkRequest: NetworkRequest? = null
): Lazy<LivedataConnectionObserver> = lazy {
    val connectionObserver = LivedataConnectionObserver(
        baseContext,
        networkRequest ?: defaultNetworkRequest
    )
    lifecycle.addObserver(connectionObserver)
    connectionObserver
}

fun Fragment.livedataConnectionObserver(
    networkRequest: NetworkRequest? = null
): Lazy<LivedataConnectionObserver> = lazy {
    val connectionObserver = LivedataConnectionObserver(
        requireContext(),
        networkRequest ?: defaultNetworkRequest
    )
    viewLifecycleOwner.lifecycle.addObserver(connectionObserver)
    connectionObserver
}

class LivedataConnectionObserver(
    context: Context,
    networkRequest: NetworkRequest,
) : ConnectionObserverImpl(context, networkRequest) {

    private val _connection: MutableLiveData<Connection> = MutableLiveData(Lost)
    val connection: LiveData<Connection> get() = _connection

    override fun onConnectionChanged(connection: Connection) {
        _connection.postValue(connection)
    }
}