package com.nutriast.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.nutriast.R
import com.nutriast.databinding.ActivityMainBinding
import com.nutriast.ui.main.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var authToken: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getLoggedInUser()
        setupBottomNavigationBar()
    }

    private fun getLoggedInUser() {
        authToken = intent.getStringExtra(EXTRA_AUTH_TOKEN).toString()
        userId = intent.getStringExtra(EXTRA_USER_ID).toString()
        val loggedInUser = listOf(authToken, userId)
        Log.d(TAG, "getLoggedInUser: $loggedInUser")
    }

    private fun setupBottomNavigationBar() {
        val bottomNav = binding.bottomNavView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_intake_history, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnItemSelectedListener { item ->
            val profileFragment = ProfileFragment.newInstance(authToken, userId)
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_intake_history -> {
                    navController.navigate(R.id.navigation_intake_history)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile, profileFragment.arguments)
                    true
                }
                else -> false
            }
        }
    }

    companion object {
        const val EXTRA_AUTH_TOKEN = "extra_auth_token"
        const val EXTRA_USER_ID = "extra_user_id"
        private val TAG = MainActivity::class.java.simpleName
    }
}