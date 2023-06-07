//package com.nutriast.ui.profile
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.preferencesDataStore
//import androidx.lifecycle.ViewModelProvider
//import com.nutriast.data.local.UserPreference
//import com.nutriast.data.remote.pojo.UserData
//import com.nutriast.databinding.ActivityProfileBinding
//import com.nutriast.helper.ViewModelFactory
//import com.nutriast.ui.login.LoginActivity
//
//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
//
//class ProfileActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityProfileBinding
//    private lateinit var profileViewModel: ProfileViewModel
//    private lateinit var authToken: String
//    private lateinit var userId: String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        supportActionBar?.hide()
//        binding = ActivityProfileBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        getLoggedInUser()
//        setupViewModel()
//        setupAction()
//        observeViewModel()
//    }
//
//    private fun getLoggedInUser() {
//        authToken = intent.getStringExtra(EXTRA_AUTH_TOKEN).toString()
//        userId = intent.getStringExtra(EXTRA_USER_ID).toString()
//        val loggedInUser = listOf(authToken, userId)
//        Log.d(TAG, "getLoggedInUser: $loggedInUser")
//    }
//
//    private fun setupViewModel() {
//        profileViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreference.getInstance(dataStore))
//        )[ProfileViewModel::class.java]
//    }
//
//    private fun setupAction() {
//        binding.logoutButton.setOnClickListener {
//            profileViewModel.logout()
//            val i = Intent(this, LoginActivity::class.java)
//            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(i)
//            finish()
//        }
//    }
//
//    private fun observeViewModel() {
//        if (userId.isNotEmpty()) {
//            profileViewModel.getUserByUserId(authToken, userId)
//        }
//
//        profileViewModel.userData.observe(this) { userData ->
//            if (userData != null) {
//                setupProfilePage(userData)
//            }
//        }
//
//        profileViewModel.apiResponse.observe(this) { response ->
//            if (response != "") { makeToast(response) }
//        }
//
//        profileViewModel.isLoading.observe(this) { isLoading ->
//            showLoading(isLoading)
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun setupProfilePage(userData: UserData) {
//        binding.apply {
//            tvUserName.text = userData.username
//            tvUserEmail.text = userData.email
//            tvUserGender.text = userData.gender
//            tvUserAge.text = if (userData.age != 1) "${userData.age} years old" else "${userData.age} year old"
//            tvUserWeight.text = "${userData.weight} kg"
//            tvUserHeight.text = "${userData.height} cm"
//        }
//    }
//
//    private fun makeToast(text: String) {
//        Toast.makeText(this@ProfileActivity, text, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun showLoading(isLoading: Boolean) {
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
//
//    companion object {
//        const val EXTRA_AUTH_TOKEN = "extra_auth_token"
//        const val EXTRA_USER_ID = "extra_user_id"
//        private val TAG = ProfileActivity::class.java.simpleName
//    }
//}