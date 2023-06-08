package com.nutriast.ui.cardiovascularprediction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nutriast.data.remote.api.ApiConfig
import com.nutriast.data.remote.pojo.PredictCardiovascularResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardiovascularPredictionViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _predictSuccessful = MutableLiveData<Boolean>()
    val predictSuccessful: LiveData<Boolean> = _predictSuccessful

    private val _apiResponse = MutableLiveData<String>()
    val apiResponse: LiveData<String> = _apiResponse

    fun predictCardiovascular(
        authToken: String, userId: String, apHi: Int, apLo: Int,
        cholesterol: Int, glucose: Int, smoke: Int, alcohol: Int, active: Int
    ) {
        _isLoading.value = true
        _predictSuccessful.value = false
        _apiResponse.value = ""
        val client = ApiConfig.getApiService().predictCardiovascularRisk(
            authToken, userId, apHi, apLo, cholesterol, glucose, smoke, alcohol, active
        )
        client.enqueue(object : Callback<PredictCardiovascularResponse> {
            override fun onResponse(
                call: Call<PredictCardiovascularResponse>,
                response: Response<PredictCardiovascularResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: $responseBody")
                    if (responseBody != null) {
                        if (responseBody.status == "success") {
                            _predictSuccessful.value = true
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
            override fun onFailure(call: Call<PredictCardiovascularResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private val TAG = CardiovascularPredictionViewModel::class.java.simpleName
    }
}