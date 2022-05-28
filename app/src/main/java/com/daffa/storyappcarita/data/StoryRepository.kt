package com.daffa.storyappcarita.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.daffa.storyappcarita.database.StoryDatabase
import com.daffa.storyappcarita.model.response.ListStoryItem
import com.daffa.storyappcarita.model.response.StoriesResponse
import com.daffa.storyappcarita.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    private var listStoryItem = MutableLiveData<List<ListStoryItem>>()

    fun getAllStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5), pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    fun getAllStoriesMap(token: String): LiveData<List<ListStoryItem>> {
        apiService.getAllStoriesLocation(token).enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    listStoryItem.postValue(response.body()?.listStory)
                    println("onresponse list $listStoryItem")
                } else {
                    Log.e("onFailure", response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e("onFailure", "Error get allStoriesMap ${t.printStackTrace()}")
            }

        })
        return listStoryItem
    }
}