package com.nutriast.data.remote.api

import com.nutriast.data.remote.pojo.LoginResponse
import com.nutriast.data.remote.pojo.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    // TODO: add list of API needed
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
}