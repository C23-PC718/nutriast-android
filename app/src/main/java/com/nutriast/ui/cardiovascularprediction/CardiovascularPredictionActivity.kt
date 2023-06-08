package com.nutriast.ui.cardiovascularprediction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nutriast.R
import com.nutriast.databinding.ActivityCardiovascularPredictionBinding

class CardiovascularPredictionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardiovascularPredictionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCardiovascularPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}