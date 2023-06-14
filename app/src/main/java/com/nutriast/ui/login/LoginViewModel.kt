package com.nutriast.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriast.data.local.UserPreference
import com.nutriast.data.remote.api.ApiConfig
import com.nutriast.data.remote.pojo.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginSuccessful = MutableLiveData<Boolean>()
    val loginSuccessful: LiveData<Boolean> = _loginSuccessful

    private val _apiResponse = MutableLiveData<String>()
    val apiResponse: LiveData<String> = _apiResponse

    private val _authToken = MutableLiveData<String>()
    val authToken: LiveData<String> = _authToken

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    fun saveLoginInfo(userId: String, token: String) {
        _authToken.value = token
        _userId.value = userId
        viewModelScope.launch {
            pref.saveLoginInfo(userId, token)
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        _loginSuccessful.value = false
        _apiResponse.value = ""
        _authToken.value = ""
        _userId.value = ""
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: $responseBody")
                    if (responseBody != null) {
                        if (responseBody.status == "success") {
                            val loginData = responseBody.data
                            val userId = loginData?.userId
                            val token = loginData?.authenticationToken
                            if (userId != null && token != null) {
                                saveLoginInfo(userId, token)
                            }
                            _loginSuccessful.value = true
                            Log.d(TAG, "onResponse: ${responseBody.message}")
                        } else {
                            Log.e(TAG, "onResponse: ${responseBody.message}")
                        }
                        _apiResponse.value = responseBody.message as String
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _apiResponse.value = response.message()
                }
                _isLoading.value = false
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }
}