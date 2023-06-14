package com.nutriast.ui.intakehistorydetail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nutriast.R
import com.nutriast.data.remote.pojo.IntakeData
import com.nutriast.databinding.ActivityIntakeHistoryDetailBinding
import java.text.SimpleDateFormat
import java.util.Locale

class IntakeHistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntakeHistoryDetailBinding
    private lateinit var mIntakeData: IntakeData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityIntakeHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntakeData()
        setupIntakeHistoryDetailPage()
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

    private fun setupIntakeHistoryDetailPage() {
        binding.apply {
            tvIntakeDate.text = changeDateFormat(mIntakeData.createdAt)
            tvUserIntakeHealthStatus.text = mIntakeData.healthstatus
            tvIntakeFeedback.text = mIntakeData.feedback
            tvUserFatIntake.text = mIntakeData.fatintake.toString()
            tvUserCaloryIntake.text = mIntakeData.caloryintake.toString()
            tvUserFiberIntake.text = mIntakeData.fiberintake.toString()
            tvUserCarbohidrateIntake.text = mIntakeData.carbohidrateintake.toString()
            tvUserProteinIntake.text = mIntakeData.proteinintake.toString()

            changeHealthStatusColor(tvUserIntakeHealthStatus.text as String)
        }
    }

    private fun changeDateFormat(createdAt: String?): String {
        if (createdAt != null) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = inputFormat.parse(createdAt)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = outputFormat.format(date!!)
            val parts = formattedDate.split("-")
            val year = parts[0]
            val month = parts[1]
            val day = parts[2]
            return "$day/$month/$year"
        } else {
            return "Date is missing"
        }
    }

    private fun changeHealthStatusColor(healthStatus: String) {
        var color = resources.getColor(R.color.black)
        if (healthStatus == "EXCELLENT") {
            color = resources.getColor(R.color.dark_green_nutriast)
        } else if (healthStatus == "POOR") {
            color = resources.getColor(R.color.red_nutriast)
        }
        binding.tvUserIntakeHealthStatus.setTextColor(color)
    }

    companion object {
        const val EXTRA_INTAKE_DATA = "extra_intake_data"
        private val TAG = IntakeHistoryDetailActivity::class.java.simpleName
    }
}