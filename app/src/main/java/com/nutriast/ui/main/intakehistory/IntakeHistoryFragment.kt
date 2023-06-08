package com.nutriast.ui.main.intakehistory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nutriast.R
import com.nutriast.databinding.FragmentHomeBinding
import com.nutriast.databinding.FragmentIntakeHistoryBinding
import com.nutriast.ui.main.home.HomeFragment

class IntakeHistoryFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var intakeHistoryViewModel: IntakeHistoryViewModel
    private lateinit var binding: FragmentIntakeHistoryBinding
    private lateinit var authToken: String
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intake_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        intakeHistoryViewModel = ViewModelProvider(this)[IntakeHistoryViewModel::class.java]
        // TODO: Use the ViewModel
    }

    companion object {
        private const val EXTRA_AUTH_TOKEN = "extra_auth_token"
        private const val EXTRA_USER_ID = "extra_user_id"
        private val TAG = IntakeHistoryFragment::class.java.simpleName

        fun newInstance(authToken: String, userId: String) = IntakeHistoryFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_AUTH_TOKEN, authToken)
                putString(EXTRA_USER_ID, userId)
            }
        }
    }

}