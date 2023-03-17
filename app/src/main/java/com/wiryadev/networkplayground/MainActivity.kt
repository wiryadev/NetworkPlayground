package com.wiryadev.networkplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wiryadev.networkplayground.databinding.ActivityMainBinding
import com.wiryadev.networkplayground.example.FragmentContainerActivity
import com.wiryadev.networkplayground.example.ObserveConnectionActivity
import com.wiryadev.networkplayground.example.ObserveConnectionComposeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnToActivity.setOnClickListener {
                ObserveConnectionActivity.start(this@MainActivity)
            }
            btnToFragment.setOnClickListener {
                FragmentContainerActivity.start(this@MainActivity)
            }
            btnToCompose.setOnClickListener {
                ObserveConnectionComposeActivity.start(this@MainActivity)
            }
        }
    }
}
