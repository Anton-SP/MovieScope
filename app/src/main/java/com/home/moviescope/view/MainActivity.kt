package com.home.moviescope.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.home.moviescope.R
import com.home.moviescope.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    ///START
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}