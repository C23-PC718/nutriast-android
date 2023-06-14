package com.nutriast.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nutriast.data.local.UserPreference
import com.nutriast.data.remote.pojo.LoginData

class SplashScreenViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<LoginData> {
        return pref.getUser().asLiveData()
    }
}