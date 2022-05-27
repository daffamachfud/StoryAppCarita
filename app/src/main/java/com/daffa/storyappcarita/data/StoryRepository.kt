package com.daffa.storyappcarita.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.daffa.storyappcarita.database.StoryDatabase
import com.daffa.storyappcarita.model.response.ListStoryItem
import com.daffa.storyappcarita.network.ApiService

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    fun getAllStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5), pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}