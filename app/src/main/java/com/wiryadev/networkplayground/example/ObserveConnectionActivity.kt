package com.wiryadev.networkplayground.example

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.wiryadev.networkplayground.connection.asString
import com.wiryadev.networkplayground.connection.flowConnectionObserver
import com.wiryadev.networkplayground.connection.livedataConnectionObserver
import com.wiryadev.networkplayground.connection.rxConnectionObserver
import com.wiryadev.networkplayground.databinding.ActivityObserveConnectionBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class ObserveConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityObserveConnectionBinding

    private val flowConnectionObserver by flowConnectionObserver()
    private val livedataConnectionObserver by livedataConnectionObserver()
    private val rxConnectionObserver by rxConnectionObserver()

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObserveConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        livedataConnectionObserver.connection.observe(this) {
            binding.tvConnectionLivedata.text = it.asString()
        }

        lifecycleScope.launch {
            flowConnectionObserver.connection
                .flowWithLifecycle(lifecycle)
                .collect {
                    binding.tvConnectionFlow.text = it.asString()
                }
        }

        val connectionDisposable = rxConnectionObserver.connection
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.tvConnectionRx.text = it.asString()
            }

        disposable.add(connectionDisposable)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, ObserveConnectionActivity::class.java))
        }
    }
}