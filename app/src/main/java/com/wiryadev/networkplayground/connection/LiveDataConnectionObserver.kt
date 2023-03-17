package com.wiryadev.networkplayground.connection

import android.content.Context
import android.net.NetworkRequest
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wiryadev.networkplayground.connection.Connection.Lost

@Composable
fun rememberLiveDataConnectionObserver(
    networkRequest: NetworkRequest? = null,
    context: Context = LocalContext.current,
    owner: LifecycleOwner = LocalLifecycleOwner.current,
): LiveDataConnectionObserver = remember(context, owner) {
    val observer = LiveDataConnectionObserver(context, networkRequest ?: defaultNetworkRequest)
    owner.lifecycle.addObserver(observer)
    observer
}

fun ComponentActivity.livedataConnectionObserver(
    networkRequest: NetworkRequest? = null
): Lazy<LiveDataConnectionObserver> = lazy {
    val connectionObserver = LiveDataConnectionObserver(
        baseContext,
        networkRequest ?: defaultNetworkRequest
    )
    lifecycle.addObserver(connectionObserver)
    connectionObserver
}

fun Fragment.livedataConnectionObserver(
    networkRequest: NetworkRequest? = null
): Lazy<LiveDataConnectionObserver> = lazy {
    val connectionObserver = LiveDataConnectionObserver(
        requireContext(),
        networkRequest ?: defaultNetworkRequest
    )
    viewLifecycleOwner.lifecycle.addObserver(connectionObserver)
    connectionObserver
}

class LiveDataConnectionObserver(
    context: Context,
    networkRequest: NetworkRequest,
) : ConnectionObserverImpl(context, networkRequest) {

    private val _connection: MutableLiveData<Connection> = MutableLiveData(Lost)
    val connection: LiveData<Connection> get() = _connection

    override fun onConnectionChanged(connection: Connection) {
        _connection.postValue(connection)
    }
}