package com.nutriast.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriast.data.local.UserPreference
import com.nutriast.data.remote.api.ApiConfig
import com.nutriast.data.remote.pojo.GetUserByUserIdResponse
import com.nutriast.data.remote.pojo.UserData
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val pref: UserPreference) : ViewModel() {
    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _apiResponse = MutableLiveData<String>()
    val apiResponse: LiveData<String> = _apiResponse

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getUserPersonalInformation(authToken: String, userId: String) {
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
                                email = user.email,
                                gender = user.gender,
                                age = user.age,
                                weight = user.weight,
                                height = user.height
                            )
                            Log.d(TAG, "onResponse: ${responseBody.message}")
                        } else {
                            Log.e(TAG, "onResponse: ${responseBody.message}")
                            _apiResponse.value = responseBody.message as String
                        }
//                        _apiResponse.value = responseBody.message as String
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

    companion object {
        private val TAG = ProfileViewModel::class.java.simpleName
    }
}