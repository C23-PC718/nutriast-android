package com.nutriast.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nutriast.data.local.UserPreference
import com.nutriast.ui.cardiovascularprediction.CardiovascularPredictionViewModel
import com.nutriast.ui.dailyintake.DailyIntakeViewModel
import com.nutriast.ui.login.LoginViewModel
import com.nutriast.ui.main.home.HomeViewModel
import com.nutriast.ui.main.intakehistory.IntakeHistoryViewModel
import com.nutriast.ui.main.profile.ProfileViewModel
import com.nutriast.ui.register.RegisterViewModel
import com.nutriast.ui.splashscreen.SplashScreenViewModel

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel() as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(pref) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel() as T
            }
            modelClass.isAssignableFrom(IntakeHistoryViewModel::class.java) -> {
                IntakeHistoryViewModel() as T
            }
            modelClass.isAssignableFrom(CardiovascularPredictionViewModel::class.java) -> {
                CardiovascularPredictionViewModel() as T
            }
            modelClass.isAssignableFrom(DailyIntakeViewModel::class.java) -> {
                DailyIntakeViewModel() as T
            }
            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) -> {
                SplashScreenViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}