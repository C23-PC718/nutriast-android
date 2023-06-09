package com.nutriast.ui.main.intakehistory

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nutriast.data.local.UserPreference
import com.nutriast.data.remote.pojo.IntakeData
import com.nutriast.databinding.FragmentIntakeHistoryBinding
import com.nutriast.helper.ViewModelFactory
import com.nutriast.ui.intakehistorydetail.IntakeHistoryDetailActivity
import com.nutriast.ui.main.MainActivity

class IntakeHistoryFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var intakeHistoryViewModel: IntakeHistoryViewModel
    private lateinit var binding: FragmentIntakeHistoryBinding
    private lateinit var adapter: IntakeHistoryAdapter
    private lateinit var authToken: String
    private lateinit var userId: String
    private lateinit var mlistUserIntake: ArrayList<IntakeData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntakeHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getLoggedInUser()
        setupViewModel()
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
        intakeHistoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
        )[IntakeHistoryViewModel::class.java]
    }

    private fun observeViewModel() {
        intakeHistoryViewModel.getUserIntakeHistory(authToken, userId)

        intakeHistoryViewModel.listUserIntake.observe(viewLifecycleOwner) { listIntakeData ->
            setupIntakeHistoryPage(listIntakeData)
        }

        intakeHistoryViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            if (response != "") { makeToast(response) }
        }

        intakeHistoryViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setupIntakeHistoryPage(listIntakeData: List<IntakeData?>?) {
        if (listIntakeData != null) {
            if (listIntakeData.isEmpty()) {
                showNoIntakeHistoryText()
            } else {
                setListUserIntake(listIntakeData)
            }
        }
    }

    private fun showNoIntakeHistoryText() {
        binding.apply {
            rvDailyIntakeHistory.visibility = View.GONE
            bottomSpace.visibility = View.GONE
            tvNoIntakeHistory.visibility = View.VISIBLE
        }
    }

    private fun setListUserIntake(listIntake: List<IntakeData?>?) {
        mlistUserIntake = ArrayList()
        if (listIntake != null) {
            for (intake in listIntake) {
                if (intake != null) {
                    mlistUserIntake.add(intake)
                }
            }
        }
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        adapter = IntakeHistoryAdapter(mlistUserIntake)
        adapter.setOnItemClickCallback(object: IntakeHistoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: IntakeData) {
                showIntakeHistoryDetail(data)
            }
        })
        binding.apply {
            rvDailyIntakeHistory.layoutManager = layoutManager
            rvDailyIntakeHistory.addItemDecoration(itemDecoration)
            rvDailyIntakeHistory.adapter = adapter
        }
    }

    private fun showIntakeHistoryDetail(intakeData: IntakeData) {
        val i = Intent(requireActivity(), IntakeHistoryDetailActivity::class.java)
        i.putExtra(IntakeHistoryDetailActivity.EXTRA_INTAKE_DATA, intakeData)
        startActivity(i)
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAG = IntakeHistoryFragment::class.java.simpleName
    }

}