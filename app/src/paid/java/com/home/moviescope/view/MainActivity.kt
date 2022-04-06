package com.home.moviescope.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import com.home.moviescope.MyTestBroadcastReceiver
import com.home.moviescope.R
import com.home.moviescope.contacts.ContactsFragment
import com.home.moviescope.databinding.ActivityMainBinding
import com.home.moviescope.geolocation.MapsFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val receiver = MyTestBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initToolbarAndDrawer()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), "MAIN")
                .commitNow()
        }
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    private fun initToolbarAndDrawer() {
        setSupportActionBar(binding.customToolbar)
        initDrawer(binding.customToolbar)
    }

    private fun initDrawer(toolbar: Toolbar) {
        //  val drawer = binding.mainDrawer
        val toggle = ActionBarDrawerToggle(
            this, binding.mainDrawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.mainDrawer.addDrawerListener(toggle)
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
                    binding.mainDrawer.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.category_2 -> {
                    Toast.makeText(
                        this,
                        "SHOW CATEGORY FROM DRAWER",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    binding.mainDrawer.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.watch_list -> {
                    supportFragmentManager.apply {
                        beginTransaction()
                            .add(R.id.container, DBFragment.newInstance())
                            .addToBackStack("")
                            .commit()
                    }
                    binding.mainDrawer.closeDrawers()
                    true
                }
                R.id.contacts -> {
                    binding.mainDrawer.closeDrawers()
                    supportFragmentManager.apply {
                        beginTransaction()
                            .add(R.id.container,ContactsFragment.newInstance())
                            .addToBackStack("")
                            .commit()
                    }
                 //   binding.mainDrawer.closeDrawers()
                    true
                }
                R.id.geolocation ->{
                    binding.mainDrawer.closeDrawers()
                    supportFragmentManager.apply {
                        beginTransaction()
                            .add(R.id.container,MapsFragment.newInstance())
                            .addToBackStack("")
                            .commit()
                    }
             //       binding.mainDrawer.closeDrawers()
                    true
                }

                else -> {
                    return@OnNavigationItemSelectedListener false
                }
            }
        })
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }



}