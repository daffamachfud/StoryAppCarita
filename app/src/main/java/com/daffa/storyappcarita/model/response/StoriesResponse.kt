package com.daffa.storyappcarita.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class StoriesResponse(

    @field:SerializedName("listStory")
	val listStory: List<ListStoryItem>,

    @field:SerializedName("error")
	val error: Boolean?,

    @field:SerializedName("message")
	val message: String?
)

@Entity(tableName = "list_story")
data class ListStoryItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lat")
	val lat: Double? = null
)
