package com.nutriast.ui.main.intakehistory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nutriast.R

class IntakeHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = IntakeHistoryFragment()
    }

    private lateinit var intakeHistoryViewModel: IntakeHistoryViewModel

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

}