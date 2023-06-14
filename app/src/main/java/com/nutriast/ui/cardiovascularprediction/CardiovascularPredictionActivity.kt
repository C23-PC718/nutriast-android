package com.nutriast.ui.cardiovascularprediction

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
import com.nutriast.databinding.ActivityCardiovascularPredictionBinding
import com.nutriast.helper.ViewModelFactory
import com.nutriast.ui.login.LoginActivity
import com.nutriast.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CardiovascularPredictionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardiovascularPredictionBinding
    private lateinit var cardiovascularPredictionViewModel: CardiovascularPredictionViewModel
    private lateinit var authToken: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCardiovascularPredictionBinding.inflate(layoutInflater)
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
        cardiovascularPredictionViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[CardiovascularPredictionViewModel::class.java]
    }

    private fun setupAction() {
        binding.predictButton.setOnClickListener {
            val apHi = binding.etApHi.text.toString().toInt()
            val apLo = binding.etApLo.text.toString().toInt()
            val cholesterolLevel = getCholesterol()
            val glucoseLevel = getGlucose()
            val isSmoke = getSmoke()
            val isAlcohol = getAlcohol()
            val isActive = getActive()

            val userPredictionData = listOf(
                apHi, apLo, cholesterolLevel, glucoseLevel, isSmoke, isAlcohol, isActive
            )
            Log.d(TAG, "setupAction userPredictionData: $userPredictionData")

            if (
                cholesterolLevel != null && glucoseLevel != null &&
                isSmoke != null && isAlcohol != null && isActive != null
            ) {
                cardiovascularPredictionViewModel.predictCardiovascular(
                    authToken, userId, apHi, apLo, cholesterolLevel,
                    glucoseLevel, isSmoke, isAlcohol, isActive
                )
            }
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        cardiovascularPredictionViewModel.predictSuccessful.observe(this) { predictSuccessful ->
            if (predictSuccessful) {
                val i = Intent(this, MainActivity::class.java)
                i.putExtra(MainActivity.EXTRA_AUTH_TOKEN, authToken)
                i.putExtra(MainActivity.EXTRA_USER_ID, userId)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            }
        }

        cardiovascularPredictionViewModel.apiResponse.observe(this) { response ->
            if (response != "") { makeToast(response) }
        }

        cardiovascularPredictionViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun getCholesterol(): Int? {
        val selectedRbCholesterol = when (binding.rgCholesterol.checkedRadioButtonId) {
            binding.rbCholesterol1.id -> 1
            binding.rbCholesterol2.id -> 2
            binding.rbCholesterol3.id -> 3
            else -> null
        }
        return selectedRbCholesterol
    }

    private fun getGlucose(): Int? {
        val selectedRbGlucose = when (binding.rgGlucose.checkedRadioButtonId) {
            binding.rbGlucose1.id -> 1
            binding.rbGlucose2.id -> 2
            binding.rbGlucose3.id -> 3
            else -> null
        }
        return selectedRbGlucose
    }

    private fun getSmoke(): Int? {
        val selectedRbSmoke = when (binding.rgSmoke.checkedRadioButtonId) {
            binding.rbSmoke1.id -> 1
            binding.rbSmoke2.id -> 0
            else -> null
        }
        return selectedRbSmoke
    }

    private fun getAlcohol(): Int? {
        val selectedRbAlcohol = when (binding.rgAlcohol.checkedRadioButtonId) {
            binding.rbAlcohol1.id -> 1
            binding.rbAlcohol2.id -> 0
            else -> null
        }
        return selectedRbAlcohol
    }

    private fun getActive(): Int? {
        val selectedRbActive = when (binding.rgActive.checkedRadioButtonId) {
            binding.rbActive1.id -> 1
            binding.rbActive2.id -> 0
            else -> null
        }
        return selectedRbActive
    }

    private fun makeToast(text: String) {
        Toast.makeText(this@CardiovascularPredictionActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_AUTH_TOKEN = "extra_auth_token"
        const val EXTRA_USER_ID = "extra_user_id"
        private val TAG = CardiovascularPredictionActivity::class.java.simpleName
    }
}