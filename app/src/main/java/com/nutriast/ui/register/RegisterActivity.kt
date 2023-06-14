package com.nutriast.ui.register

import android.app.DatePickerDialog
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
import com.nutriast.databinding.ActivityRegisterBinding
import com.nutriast.helper.ViewModelFactory
import com.nutriast.ui.login.LoginActivity
import java.util.Calendar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
        observeViewModel()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val username = binding.etName.text.toString()
            val birthdate = binding.etDateOfBirth.text.toString()
            val gender = getGender()
            val height = binding.etHeight.text.toString().toInt()
            val weight = binding.etWeight.text.toString().toInt()

            val newUserData = listOf(email, password, username, birthdate, gender, height, weight)
            Log.d(TAG, "setupAction newUserData: $newUserData")

            if (
                email != "" && password != "" && username != "" && birthdate != "" && gender != null
            ) {
                val birthdateFormatted = changeBirthDateFormat(birthdate)
                registerViewModel.register(
                    email, password, username, birthdateFormatted, gender, height, weight
                )
            } else {
                makeToast("Please fill all fields")
            }
        }
    }

    private fun observeViewModel() {
        registerViewModel.registerSuccessful.observe(this) { registerSuccessful ->
            if (registerSuccessful) {
                val i = Intent(this, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            }
        }

        registerViewModel.apiResponse.observe(this) { response ->
            if (response != "") { makeToast(response) }
        }

        registerViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    fun showDatePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            binding.etDateOfBirth.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun getGender(): String? {
        val selectedRbGender = when (binding.rgGender.checkedRadioButtonId) {
            binding.radioButtonMale.id -> "male"
            binding.radioButtonFemale.id -> "female"
            else -> null
        }
        return selectedRbGender
    }

    private fun changeBirthDateFormat(birthdate: String): String {
        val birthdateParts = birthdate.split("/")
        val day = birthdateParts[0]
        val month = birthdateParts[1]
        val year = birthdateParts[2]
        return "$year-$month-$day"
    }

    private fun makeToast(text: String) {
        Toast.makeText(this@RegisterActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
    }
}