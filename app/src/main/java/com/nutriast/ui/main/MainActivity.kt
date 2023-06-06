package com.nutriast.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.nutriast.databinding.ActivityMainBinding
import com.nutriast.ui.profile.ProfileActivity

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

        binding.profileButton.setOnClickListener {
            val i = Intent(this, ProfileActivity::class.java)
            i.putExtra(ProfileActivity.EXTRA_AUTH_TOKEN, authToken)
            i.putExtra(ProfileActivity.EXTRA_USER_ID, userId)
            startActivity(i)
        }
    }

    private fun getLoggedInUser() {
        authToken = intent.getStringExtra(EXTRA_AUTH_TOKEN).toString()
        userId = intent.getStringExtra(EXTRA_USER_ID).toString()
        val loggedInUser = listOf(authToken, userId)
        Log.d(TAG, "getLoggedInUser: $loggedInUser")
    }

    companion object {
        const val EXTRA_AUTH_TOKEN = "extra_auth_token"
        const val EXTRA_USER_ID = "extra_user_id"
        private val TAG = MainActivity::class.java.simpleName
    }
}