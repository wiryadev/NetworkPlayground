package com.wiryadev.networkplayground.connection

import android.content.Context
import android.net.NetworkRequest
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.wiryadev.networkplayground.connection.Connection.Lost
import io.reactivex.rxjava3.core.Emitter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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