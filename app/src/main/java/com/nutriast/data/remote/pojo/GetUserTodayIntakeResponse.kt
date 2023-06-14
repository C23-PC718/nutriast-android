package com.nutriast.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class GetUserTodayIntakeResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: TodayIntakeData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class TodayIntakeData(

	@field:SerializedName("feedback")
	val feedback: String? = null,

	@field:SerializedName("healthstatus")
	val healthstatus: String? = null
)
