package com.nutriast.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nutriast.data.remote.api.ApiConfig
import com.nutriast.data.remote.pojo.RegisterResponse
import com.nutriast.ui.login.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerSuccessful = MutableLiveData<Boolean>()
    val registerSuccessful: LiveData<Boolean> = _registerSuccessful

    private val _apiResponse = MutableLiveData<String>()
    val apiResponse: LiveData<String> = _apiResponse

    fun register(
        email: String, password: String, username: String, birthdate: String,
        gender: String, height: Int, weight: Int
    ) {
        _isLoading.value = true
        _registerSuccessful.value = false
        _apiResponse.value = ""
        val client = ApiConfig.getApiService()
            .register(email, password, username, birthdate, gender, height, weight)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: $responseBody")
                    if (responseBody != null) {
                        if (responseBody.status == "success") {
                            _registerSuccessful.value = true
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
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private val TAG = RegisterViewModel::class.java.simpleName
    }
}