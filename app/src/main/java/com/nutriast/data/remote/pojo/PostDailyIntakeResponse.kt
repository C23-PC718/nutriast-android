package com.nutriast.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class PostDailyIntakeResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: PostIntakeData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PostIntakeData(

	@field:SerializedName("feedback")
	val feedback: String? = null,

	@field:SerializedName("proteinintake")
	val proteinintake: Double? = null,

	@field:SerializedName("fiberintake")
	val fiberintake: Double? = null,

	@field:SerializedName("healthstatus")
	val healthstatus: String? = null,

	@field:SerializedName("carbohidrateintake")
	val carbohidrateintake: Double? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userid")
	val userid: String? = null,

	@field:SerializedName("fatintake")
	val fatintake: Double? = null,

	@field:SerializedName("caloryintake")
	val caloryintake: Double? = null
)
