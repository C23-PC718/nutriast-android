package com.nutriast.ui.main.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nutriast.data.local.UserPreference
import com.nutriast.data.remote.pojo.UserData
import com.nutriast.databinding.FragmentProfileBinding
import com.nutriast.helper.ViewModelFactory
import com.nutriast.ui.login.LoginActivity
import com.nutriast.ui.main.MainActivity

class ProfileFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var authToken: String
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getLoggedInUser()
        setupViewModel()
        setupAction()
        observeViewModel()
    }

    private fun getLoggedInUser() {
        val mainActivity = requireActivity() as MainActivity
        val loggedInUser = mainActivity.getLoggedInUser()
        authToken = loggedInUser[0]
        userId = loggedInUser[1]
        Log.d(TAG, "getLoggedInUser: $loggedInUser")
    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[ProfileViewModel::class.java]
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
            val i = Intent(requireActivity(), LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            requireActivity().finish()
        }
    }

    private fun observeViewModel() {
        if (userId.isNotEmpty()) {
            profileViewModel.getUserByUserId(authToken, userId)
        }

        profileViewModel.userData.observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                setupProfilePage(userData)
            }
        }

        profileViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            if (response != "") { makeToast(response) }
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupProfilePage(userData: UserData) {
        binding.apply {
            tvUserName.text = userData.username
            tvUserEmail.text = userData.email
            tvUserGender.text = userData.gender
            tvUserAge.text = if (userData.age != 1) "${userData.age} years old" else "${userData.age} year old"
            tvUserWeight.text = "${userData.weight} kg"
            tvUserHeight.text = "${userData.height} cm"
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAG = ProfileFragment::class.java.simpleName
    }
}