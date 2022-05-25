package com.daffa.storyappcarita.network

import com.daffa.storyappcarita.model.LoginResponse
import com.daffa.storyappcarita.model.ServiceResponse
import com.daffa.storyappcarita.model.StoriesResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ServiceResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: String
    ): Call<ServiceResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") authHeader: String
    ): Call<StoriesResponse>
}