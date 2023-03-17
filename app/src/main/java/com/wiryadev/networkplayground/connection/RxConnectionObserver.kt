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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject

@Composable
fun rememberRxConnectionObserver(
    networkRequest: NetworkRequest? = null,
    context: Context = LocalContext.current,
    owner: LifecycleOwner = LocalLifecycleOwner.current,
): RxConnectionObserver = remember(context, owner) {
    val observer = RxConnectionObserver(context, networkRequest ?: defaultNetworkRequest)
    owner.lifecycle.addObserver(observer)
    observer
}

fun ComponentActivity.rxConnectionObserver(
    networkRequest: NetworkRequest? = null
): Lazy<RxConnectionObserver> = lazy {
    val connectionObserver = RxConnectionObserver(
        baseContext,
        networkRequest ?: defaultNetworkRequest
    )
    lifecycle.addObserver(connectionObserver)
    connectionObserver
}

fun Fragment.rxConnectionObserver(
    networkRequest: NetworkRequest? = null
): Lazy<RxConnectionObserver> = lazy {
    val connectionObserver = RxConnectionObserver(
        requireContext(),
        networkRequest ?: defaultNetworkRequest
    )
    viewLifecycleOwner.lifecycle.addObserver(connectionObserver)
    connectionObserver
}

class RxConnectionObserver(
    context: Context,
    networkRequest: NetworkRequest,
) : ConnectionObserverImpl(context, networkRequest) {

    private val _connection: Subject<Connection> = BehaviorSubject.create()
    val connection: Observable<Connection> = _connection.map { it }

    override fun onConnectionChanged(connection: Connection) {
        _connection.onNext(connection)
    }
}