package com.nutriast.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.nutriast.data.local.UserPreference
import com.nutriast.databinding.ActivitySplashScreenBinding
import com.nutriast.helper.ViewModelFactory
import com.nutriast.ui.login.LoginActivity
import com.nutriast.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var splashScreenViewModel: SplashScreenViewModel
    private lateinit var authToken: String
    private lateinit var userId: String
    private var userLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        binding.ivSplashScreen.alpha = 0f
        binding.ivSplashScreen.animate().setDuration(2500).alpha(1f).withEndAction {
            splashScreenViewModel.getUser().observe(this) { user ->
                if (!user.authenticationToken.isNullOrEmpty()) {
                    authToken = user.authenticationToken.toString()
                    userId = user.userId.toString()
                    userLoggedIn = true
                }
                moveActivity()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }

    private fun setupViewModel() {
        splashScreenViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SplashScreenViewModel::class.java]
    }

    private fun moveActivity() {
        if (userLoggedIn) {
            val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
            i.putExtra(MainActivity.EXTRA_AUTH_TOKEN, authToken)
            i.putExtra(MainActivity.EXTRA_USER_ID, userId)
            startActivity(i)
        } else {
            val i = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(i)
        }
    }

    companion object {
        private val TAG = SplashScreenActivity::class.java.simpleName
    }
}