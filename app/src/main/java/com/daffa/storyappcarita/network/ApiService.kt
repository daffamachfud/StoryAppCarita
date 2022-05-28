package com.daffa.storyappcarita.network

import com.daffa.storyappcarita.model.response.ListStoryItem
import com.daffa.storyappcarita.model.response.LoginResponse
import com.daffa.storyappcarita.model.response.ServiceResponse
import com.daffa.storyappcarita.model.response.StoriesResponse
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
        @Header("Authorization") authHeader: String,
    ): Call<StoriesResponse>

    @GET("stories")
    suspend fun getNewStories(
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoriesResponse

    @GET("stories?location=1")
    fun getAllStoriesLocation(
        @Header("Authorization") token: String
    ): Call<StoriesResponse>
}