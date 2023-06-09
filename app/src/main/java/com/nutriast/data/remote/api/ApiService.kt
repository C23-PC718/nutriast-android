package com.nutriast.data.remote.api

import com.nutriast.data.remote.pojo.GetUserByUserIdResponse
import com.nutriast.data.remote.pojo.GetUserIntakeHistoryResponse
import com.nutriast.data.remote.pojo.GetUserTodayIntakeResponse
import com.nutriast.data.remote.pojo.LoginResponse
import com.nutriast.data.remote.pojo.PostDailyIntakeResponse
import com.nutriast.data.remote.pojo.PredictCardiovascularResponse
import com.nutriast.data.remote.pojo.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("username") username: String,
        @Field("birthdate") birthdate: String,
        @Field("gender") gender: String,
        @Field("height") height: Int,
        @Field("weight") weight: Int
    ): Call<RegisterResponse>

    @GET("users/{userId}")
    fun getUserByUserId(
        @Header("Authorization") authToken: String,
        @Path("userId") userId: String
    ): Call<GetUserByUserIdResponse>

    @FormUrlEncoded
    @POST("predict/{userId}")
    fun predictCardiovascularRisk(
        @Header("Authorization") authToken: String,
        @Path("userId") userId: String,
        @Field("ap_hi") apHi: Int,
        @Field("ap_lo") apLo: Int,
        @Field("cholesterol") cholesterol: Int,
        @Field("gluc") glucose: Int,
        @Field("smoke") smoke: Int,
        @Field("alco") alcohol: Int,
        @Field("active") active: Int
    ): Call<PredictCardiovascularResponse>

    @GET("intakeusershistory/{userId}")
    fun getUserIntakeHistory(
        @Header("Authorization") authToken: String,
        @Path("userId") userId: String
    ): Call<GetUserIntakeHistoryResponse>

    @GET("intakeusers/{userId}")
    fun getUserTodayIntake(
        @Header("Authorization") authToken: String,
        @Path("userId") userId: String
    ): Call<GetUserTodayIntakeResponse>

    @FormUrlEncoded
    @POST("intakeusers/{userId}")
    fun postDailyIntake(
        @Header("Authorization") authToken: String,
        @Path("userId") userId: String,
        @Field("rice") rice: Double,
        @Field("egg") egg: Double,
        @Field("chicken") chicken: Double,
        @Field("beef") beef: Double,
        @Field("fish") fish: Double,
        @Field("tofu") tofu: Double,
        @Field("soybean_cake") soybeanCake: Double,
        @Field("noodle") noodle: Double,
        @Field("potato") potato: Double,
        @Field("milk") milk: Double,
        @Field("apple") apple: Double,
        @Field("watermelon") watermelon: Double,
        @Field("banana") banana: Double,
        @Field("avocado") avocado: Double,
        @Field("papaya") papaya: Double
    ): Call<PostDailyIntakeResponse>
}