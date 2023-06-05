package com.nutriast.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.nutriast.data.local.UserPreference
import com.nutriast.databinding.ActivityLoginBinding
import com.nutriast.helper.ViewModelFactory
import com.nutriast.ui.main.MainActivity
import com.nutriast.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mAuthToken: String
    private lateinit var mUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
        observeViewModel()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            loginViewModel.login(email, password)
        }

        binding.tvRegisterNow.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
    }

    private fun observeViewModel() {
        loginViewModel.authToken.observe(this) { authToken ->
            mAuthToken = authToken
        }

        loginViewModel.userId.observe(this) { userId ->
            mUserId = userId
        }

        loginViewModel.loginSuccessful.observe(this) { loginSuccessful ->
            if (loginSuccessful) {
                val i = Intent(this, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                i.putExtra(MainActivity.EXTRA_AUTH_TOKEN, mAuthToken)
                i.putExtra(MainActivity.EXTRA_USER_ID, mUserId)
                startActivity(i)
                finish()
            }
        }

        loginViewModel.apiResponse.observe(this) { response ->
            if (response != "") { makeToast(response) }
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(this@LoginActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}