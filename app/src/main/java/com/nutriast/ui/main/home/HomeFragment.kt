package com.nutriast.ui.main.home

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
import com.nutriast.R
import com.nutriast.data.local.UserPreference
import com.nutriast.data.remote.pojo.UserData
import com.nutriast.databinding.FragmentHomeBinding
import com.nutriast.helper.ViewModelFactory
import com.nutriast.ui.cardiovascularprediction.CardiovascularPredictionActivity
import com.nutriast.ui.dailyintake.DailyIntakeActivity
import com.nutriast.ui.main.MainActivity

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
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[HomeViewModel::class.java]
    }

    private fun setupAction() {
        binding.intakeFormButton.setOnClickListener {
            val i = Intent(requireActivity(), DailyIntakeActivity::class.java)
            i.putExtra(CardiovascularPredictionActivity.EXTRA_AUTH_TOKEN, authToken)
            i.putExtra(CardiovascularPredictionActivity.EXTRA_USER_ID, userId)
            startActivity(i)
        }

        binding.predictCardiovascularRiskButton.setOnClickListener {
            val i = Intent(requireActivity(), CardiovascularPredictionActivity::class.java)
            i.putExtra(CardiovascularPredictionActivity.EXTRA_AUTH_TOKEN, authToken)
            i.putExtra(CardiovascularPredictionActivity.EXTRA_USER_ID, userId)
            startActivity(i)
        }
    }

    private fun observeViewModel() {
        homeViewModel.getUserData(authToken, userId)
        homeViewModel.getTodayIntakeInformation(authToken, userId)

        homeViewModel.userData.observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                setupHomePage(userData)
            }
        }

        homeViewModel.userTodayIntake.observe(viewLifecycleOwner) { userTodayIntake ->
            if (userTodayIntake != null) {
                val healthStatus = userTodayIntake[0]
                val feedback = userTodayIntake[1]
                setupUserTodayIntake(healthStatus, feedback)
            }
        }

        homeViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            if (response != "") { makeToast(response) }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setupHomePage(userData: UserData) {
        binding.apply {
            tvUserName.text = userData.username
            tvUserFatNeed.text = (userData.fatneed as Double).toString()
            tvUserCaloryNeed.text = (userData.caloryneed as Double).toString()
            tvUserFiberNeed.text = (userData.fiberneed as Double).toString()
            tvUserCarbohidrateNeed.text = (userData.carbohidrateneed as Double).toString()
            tvUserProteinNeed.text = (userData.proteinneed as Double).toString()
            if (userData.cardiovascular != null) {
                tvUserCardiovascularRisk.text = userData.cardiovascular as String
                changeCardiovascularRiskColor(tvUserCardiovascularRisk.text as String)
            }
        }
    }

    private fun setupUserTodayIntake(healthStatus: String?, feedback: String?) {
        if (healthStatus != null && feedback != null) {
            binding.apply {
                tvUserHealthStatus.text = healthStatus
                tvFeedback.text = feedback

                changeHealthStatusColor(tvUserHealthStatus.text as String)
            }
        }
    }

    private fun changeHealthStatusColor(healthStatus: String) {
        var color = resources.getColor(R.color.black)
        if (healthStatus == "EXCELLENT") {
            color = resources.getColor(R.color.dark_green_nutriast)
        } else if (healthStatus == "POOR") {
            color = resources.getColor(R.color.red_nutriast)
        }
        binding.tvUserHealthStatus.setTextColor(color)
    }

    private fun changeCardiovascularRiskColor(cardiovascularRisk: String) {
        var color = resources.getColor(R.color.black)
        if (cardiovascularRisk == "Safe") {
            color = resources.getColor(R.color.dark_green_nutriast)
        } else if (cardiovascularRisk == "Aware") {
            color = resources.getColor(R.color.red_nutriast)
        }
        binding.tvUserCardiovascularRisk.setTextColor(color)
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }
}