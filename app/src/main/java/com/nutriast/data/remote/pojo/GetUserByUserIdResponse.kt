package com.nutriast.data.remote.pojo

import com.google.gson.annotations.SerializedName

data class GetUserByUserIdResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: UserData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UserData(

	@field:SerializedName("carbohidrateneed")
	val carbohidrateneed: Any? = null,

	@field:SerializedName("caloryneed")
	val caloryneed: Any? = null,

	@field:SerializedName("birthdate")
	val birthdate: String? = null,

	@field:SerializedName("proteinneed")
	val proteinneed: Any? = null,

	@field:SerializedName("fiberneed")
	val fiberneed: Any? = null,

	@field:SerializedName("fatneed")
	val fatneed: Any? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("cardiovascular")
	val cardiovascular: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("height")
	val height: Int? = null
)
