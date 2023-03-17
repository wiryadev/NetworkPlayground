package com.wiryadev.networkplayground.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wiryadev.networkplayground.R

class FragmentContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ObserveConnectionFragment.newInstance())
                .commitNow()
        }
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, FragmentContainerActivity::class.java))
        }
    }
}