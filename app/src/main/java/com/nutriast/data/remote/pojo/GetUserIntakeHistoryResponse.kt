package com.nutriast.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class GetUserIntakeHistoryResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<IntakeData?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class IntakeData(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userid")
	val userid: String? = null,

	@field:SerializedName("fatintake")
	val fatintake: Double? = null,

	@field:SerializedName("proteinintake")
	val proteinintake: Double? = null,

	@field:SerializedName("caloryintake")
	val caloryintake: Double? = null,

	@field:SerializedName("fiberintake")
	val fiberintake: Double? = null,

	@field:SerializedName("carbohidrateintake")
	val carbohidrateintake: Double? = null,

	@field:SerializedName("healthstatus")
	val healthstatus: String? = null,

	@field:SerializedName("feedback")
	val feedback: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
