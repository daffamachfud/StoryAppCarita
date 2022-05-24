package com.daffa.storyappcarita.network

import com.daffa.storyappcarita.model.LoginResponse
import com.daffa.storyappcarita.model.StoriesResponse
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class ApiService {

    companion object {
        const val URL_END_POINT = "https://story-api.dicoding.dev/v1/"
    }

    data class ResponseService(
        @field:SerializedName("error")
        val error: Boolean,
        @field:SerializedName("message")
        val message: String
    )

    interface ApiService {
        @FormUrlEncoded
        @POST("register")
        fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("password") password: String
        ): Call<ResponseService>

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
        ): Call<ResponseService>

        @GET("stories")
        fun getStories(
            @Header("Authorization") authHeader: String
        ): Call<StoriesResponse>
    }

    class ApiConfig {
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(URL_END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}