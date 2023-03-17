package com.wiryadev.networkplayground.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.wiryadev.networkplayground.connection.asString
import com.wiryadev.networkplayground.connection.flowConnectionObserver
import com.wiryadev.networkplayground.connection.livedataConnectionObserver
import com.wiryadev.networkplayground.connection.rxConnectionObserver
import com.wiryadev.networkplayground.databinding.FragmentObserveConnectionBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class ObserveConnectionFragment : Fragment() {

    private var _binding: FragmentObserveConnectionBinding? = null
    private val binding: FragmentObserveConnectionBinding get() = _binding!!

    private val flowConnectionObserver by flowConnectionObserver()
    private val livedataConnectionObserver by livedataConnectionObserver()
    private val rxConnectionObserver by rxConnectionObserver()

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObserveConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        livedataConnectionObserver.connection.observe(viewLifecycleOwner) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable.dispose()
    }

    companion object {
        fun newInstance() = ObserveConnectionFragment()
    }
}