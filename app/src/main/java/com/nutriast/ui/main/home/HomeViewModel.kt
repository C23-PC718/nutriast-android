package com.nutriast.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nutriast.data.remote.api.ApiConfig
import com.nutriast.data.remote.pojo.GetUserByUserIdResponse
import com.nutriast.data.remote.pojo.GetUserTodayIntakeResponse
import com.nutriast.data.remote.pojo.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?> = _userData

    private val _userTodayIntake = MutableLiveData<List<String?>?>()
    val userTodayIntake: LiveData<List<String?>?> = _userTodayIntake

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _apiResponse = MutableLiveData<String>()
    val apiResponse: LiveData<String> = _apiResponse

    fun getUserData(authToken: String, userId: String) {
        _isLoading.value = true
        _apiResponse.value = ""
        val client = ApiConfig.getApiService().getUserByUserId("Bearer $authToken", userId)
        client.enqueue(object : Callback<GetUserByUserIdResponse> {
            override fun onResponse(
                call: Call<GetUserByUserIdResponse>,
                response: Response<GetUserByUserIdResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: $responseBody")
                    if (responseBody != null) {
                        if (responseBody.status == "success" && responseBody.data != null) {
                            val user = responseBody.data
                            _userData.value = UserData(
                                username = user.username,
                                cardiovascular = user.cardiovascular,
                                fatneed = user.fatneed,
                                caloryneed = user.caloryneed,
                                fiberneed = user.fiberneed,
                                carbohidrateneed = user.carbohidrateneed,
                                proteinneed = user.proteinneed
                            )
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
            override fun onFailure(call: Call<GetUserByUserIdResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    fun getTodayIntakeInformation(authToken: String, userId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .getUserTodayIntake("Bearer $authToken", userId)
        client.enqueue(object : Callback<GetUserTodayIntakeResponse> {
            override fun onResponse(
                call: Call<GetUserTodayIntakeResponse>,
                response: Response<GetUserTodayIntakeResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: $responseBody")
                    if (responseBody != null) {
                        if (responseBody.status == "success" && responseBody.data != null) {
                            val healthStatus = responseBody.data.healthstatus
                            val feedback = responseBody.data.feedback
                            _userTodayIntake.value = listOf(healthStatus, feedback)
                            Log.d(TAG, "onResponse: ${responseBody.message}")
                        } else {
                            Log.e(TAG, "onResponse: ${responseBody.message}")
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }
            override fun onFailure(call: Call<GetUserTodayIntakeResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }
}