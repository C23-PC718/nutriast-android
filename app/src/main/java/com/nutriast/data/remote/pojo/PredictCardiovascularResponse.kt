package com.nutriast.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class PredictCardiovascularResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: PredictCardiovascularData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PredictCardiovascularData(

	@field:SerializedName("prediction")
	val prediction: Int? = null
)
