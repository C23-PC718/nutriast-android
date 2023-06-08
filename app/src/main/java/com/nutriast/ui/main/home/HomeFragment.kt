package com.nutriast.ui.main.home

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nutriast.R
import com.nutriast.data.local.UserPreference
import com.nutriast.databinding.FragmentHomeBinding
import com.nutriast.helper.ViewModelFactory
import com.nutriast.ui.main.MainActivity
import com.nutriast.ui.main.MainActivity.Companion.EXTRA_AUTH_TOKEN
import com.nutriast.ui.main.MainActivity.Companion.EXTRA_USER_ID
import com.nutriast.ui.main.profile.ProfileFragment
import com.nutriast.ui.main.profile.ProfileViewModel

class HomeFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var authToken: String
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getLoggedInUser()
        setupViewModel()
    }

    private fun getLoggedInUser() {
        val mainActivity = requireActivity() as MainActivity
        val loggedInUser = mainActivity.getLoggedInUser()
        authToken = loggedInUser[0]
        userId = loggedInUser[1]
        Log.d(TAG, "getLoggedInUser: $loggedInUser")
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[HomeViewModel::class.java]
    }

    private fun setupAction() {
        //
    }

    private fun observeViewModel() {
        //
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }
}