package com.nutriast.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nutriast.databinding.ActivityMainBinding
import com.nutriast.ui.login.LoginActivity
import com.nutriast.ui.profile.ProfileActivity
import com.nutriast.ui.register.RegisterActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var authToken: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authToken = intent.getStringExtra(EXTRA_AUTH_TOKEN).toString()
        userId = intent.getStringExtra(EXTRA_USER_ID).toString()
        val loggedInUser = listOf(authToken, userId)
        Log.d(TAG, "onCreate loggedInUser: $loggedInUser")

        binding.profileButton.setOnClickListener {
            val i = Intent(this, ProfileActivity::class.java)
            startActivity(i)
        }
    }

    companion object {
        const val EXTRA_AUTH_TOKEN = "extra_auth_token"
        const val EXTRA_USER_ID = "extra_user_id"
        private val TAG = MainActivity::class.java.simpleName
    }
}