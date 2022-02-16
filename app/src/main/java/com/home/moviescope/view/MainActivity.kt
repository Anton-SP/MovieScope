package com.home.moviescope.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import com.home.moviescope.R
import com.home.moviescope.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initToolbarAndDrawer()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    private fun initToolbarAndDrawer() {
        val toolbar = binding.customToolbar
        setSupportActionBar(toolbar)
        initDrawer(toolbar)
    }

    private fun initDrawer(toolbar: Toolbar) {

        val drawer = binding.mainDrawer
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = binding.navigationView
        navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.category_1 -> {
                    Toast.makeText(
                        this,
                        "SHOW CATEGORY FROM DRAWER",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    /*  supportFragmentManager.beginTransaction()
                          .replace(R.id.container, CategoryDetailedFragment.newInstance())
                          .addToBackStack(null)
                          .commit()*/

                    return@OnNavigationItemSelectedListener true

                }
                R.id.category_2 -> {
                    Toast.makeText(
                        this,
                        "SHOW CATEGORY FROM DRAWER",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    /*supportFragmentManager.beginTransaction()
                        .replace(R.id.container, CategoryDetailedFragment.newInstance())
                        .addToBackStack(null)
                        .commit()*/

                    return@OnNavigationItemSelectedListener true
                }
                else -> {
                    return@OnNavigationItemSelectedListener false
                }
            }

        })


    }
}