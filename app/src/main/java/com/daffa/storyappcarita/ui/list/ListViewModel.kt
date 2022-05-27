package com.daffa.storyappcarita.ui.list

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.daffa.storyappcarita.data.StoryRepository
import com.daffa.storyappcarita.model.response.ListStoryItem
import com.daffa.storyappcarita.model.response.LoginResult
import com.daffa.storyappcarita.util.UserPreference

class ListViewModel(
    private val storyRepository: StoryRepository,
    private val pref: UserPreference
) : ViewModel() {

    fun getPagingStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getAllStories(token).cachedIn(viewModelScope)
    }

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }
}