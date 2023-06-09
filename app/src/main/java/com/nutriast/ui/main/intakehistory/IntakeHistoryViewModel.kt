package com.nutriast.ui.main.intakehistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nutriast.data.remote.api.ApiConfig
import com.nutriast.data.remote.pojo.GetUserIntakeHistoryResponse
import com.nutriast.data.remote.pojo.IntakeData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IntakeHistoryViewModel : ViewModel() {
    private val _listUserIntake = MutableLiveData<List<IntakeData?>?>()
    val listUserIntake: LiveData<List<IntakeData?>?> = _listUserIntake

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _apiResponse = MutableLiveData<String>()
    val apiResponse: LiveData<String> = _apiResponse

    fun getUserIntakeHistory(authToken: String, userId: String) {
        _isLoading.value = true
        _apiResponse.value = ""
        val client = ApiConfig.getApiService()
            .getUserIntakeHistory("Bearer $authToken", userId)
        client.enqueue(object : Callback<GetUserIntakeHistoryResponse> {
            override fun onResponse(
                call: Call<GetUserIntakeHistoryResponse>,
                response: Response<GetUserIntakeHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: $responseBody")
                    if (responseBody != null) {
                        if (responseBody.status == "success" && responseBody.data != null) {
                            val listIntakeData = responseBody.data
                            _listUserIntake.value = listIntakeData
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
            override fun onFailure(call: Call<GetUserIntakeHistoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private val TAG = IntakeHistoryViewModel::class.java.simpleName
    }
}