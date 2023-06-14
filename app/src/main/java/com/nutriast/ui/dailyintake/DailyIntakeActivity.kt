package com.nutriast.ui.dailyintake

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.nutriast.data.local.UserPreference
import com.nutriast.databinding.ActivityDailyIntakeBinding
import com.nutriast.helper.ViewModelFactory
import com.nutriast.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DailyIntakeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDailyIntakeBinding
    private lateinit var dailyIntakeViewModel: DailyIntakeViewModel
    private lateinit var authToken: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDailyIntakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getLoggedInUser()
        setupViewModel()
        setupAction()
        observeViewModel()
    }

    private fun getLoggedInUser() {
        authToken = intent.getStringExtra(EXTRA_AUTH_TOKEN).toString()
        userId = intent.getStringExtra(EXTRA_USER_ID).toString()
        val loggedInUser = listOf(authToken, userId)
        Log.d(TAG, "getLoggedInUser: $loggedInUser")
    }

    private fun setupViewModel() {
        dailyIntakeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DailyIntakeViewModel::class.java]
    }

    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            val rice = binding.etRice.text.toString().toDouble()
            val egg = binding.etEgg.text.toString().toDouble()
            val chicken = binding.etChicken.text.toString().toDouble()
            val beef = binding.etBeef.text.toString().toDouble()
            val fish = binding.etFish.text.toString().toDouble()
            val tofu = binding.etTofu.text.toString().toDouble()
            val soybeanCake = binding.etSoybeanCake.text.toString().toDouble()
            val noodle = binding.etNoodle.text.toString().toDouble()
            val potato = binding.etPotato.text.toString().toDouble()
            val milk = binding.etMilk.text.toString().toDouble()
            val apple = binding.etApple.text.toString().toDouble()
            val watermelon = binding.etWatermelon.text.toString().toDouble()
            val banana = binding.etBanana.text.toString().toDouble()
            val avocado = binding.etAvocado.text.toString().toDouble()
            val papaya = binding.etPapaya.text.toString().toDouble()

            val userTodayIntake = listOf(
                rice, egg, chicken, beef, fish, tofu, soybeanCake, noodle, potato,
                milk, apple, watermelon, banana, avocado, papaya
            )
            Log.d(TAG, "setupAction userTodayIntake: $userTodayIntake")

            dailyIntakeViewModel.postDailyIntake(
                authToken, userId, rice, egg, chicken, beef, fish, tofu, soybeanCake,
                noodle, potato, milk, apple, watermelon, banana, avocado, papaya
            )
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        dailyIntakeViewModel.postSuccessful.observe(this) { postSuccessful ->
            if (postSuccessful) {
                val i = Intent(this, MainActivity::class.java)
                i.putExtra(MainActivity.EXTRA_AUTH_TOKEN, authToken)
                i.putExtra(MainActivity.EXTRA_USER_ID, userId)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            }
        }

        dailyIntakeViewModel.apiResponse.observe(this) { response ->
            if (response != "") { makeToast(response) }
        }

        dailyIntakeViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(this@DailyIntakeActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_AUTH_TOKEN = "extra_auth_token"
        const val EXTRA_USER_ID = "extra_user_id"
        private val TAG = DailyIntakeActivity::class.java.simpleName
    }
}