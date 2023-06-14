package com.nutriast.ui.dailyintake

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nutriast.data.remote.api.ApiConfig
import com.nutriast.data.remote.pojo.PostDailyIntakeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DailyIntakeViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _postSuccessful = MutableLiveData<Boolean>()
    val postSuccessful: LiveData<Boolean> = _postSuccessful

    private val _apiResponse = MutableLiveData<String>()
    val apiResponse: LiveData<String> = _apiResponse

    fun postDailyIntake(
        authToken: String, userId: String, rice: Double, egg: Double, chicken: Double, beef: Double,
        tofu: Double, soybeanCake: Double, noodle: Double, potato: Double, milk: Double,
        coffee: Double, tea: Double, fish: Double, banana: Double, avocado: Double, papaya: Double
    ) {
        _isLoading.value = true
        _postSuccessful.value = false
        _apiResponse.value = ""
        val client = ApiConfig.getApiService().postDailyIntake(
            "Bearer $authToken", userId, rice, egg, chicken, beef, tofu, soybeanCake,
            noodle, potato, milk, coffee, tea, fish, banana, avocado, papaya
        )
        client.enqueue(object : Callback<PostDailyIntakeResponse> {
            override fun onResponse(
                call: Call<PostDailyIntakeResponse>,
                response: Response<PostDailyIntakeResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d(TAG, "onResponse: $responseBody")
                    if (responseBody != null) {
                        if (responseBody.status == "success") {
                            _postSuccessful.value = true
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
            override fun onFailure(call: Call<PostDailyIntakeResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private val TAG = DailyIntakeViewModel::class.java.simpleName
    }
}