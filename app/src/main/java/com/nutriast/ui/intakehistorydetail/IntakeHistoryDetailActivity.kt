package com.nutriast.ui.intakehistorydetail

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.nutriast.data.remote.pojo.IntakeData
import com.nutriast.databinding.ActivityIntakeHistoryDetailBinding

class IntakeHistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntakeHistoryDetailBinding
    private lateinit var mIntakeData: IntakeData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityIntakeHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntakeData()
    }

    private fun getIntakeData() {
        val intakeData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_INTAKE_DATA, IntakeData::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_INTAKE_DATA)
        }

        if (intakeData != null) {
            mIntakeData = intakeData
        }
    }

    companion object {
        const val EXTRA_INTAKE_DATA = "extra_intake_data"
        private val TAG = IntakeHistoryDetailActivity::class.java.simpleName
    }
}