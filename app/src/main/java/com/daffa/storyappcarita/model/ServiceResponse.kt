package com.daffa.storyappcarita.model

import com.google.gson.annotations.SerializedName

data class ServiceResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)