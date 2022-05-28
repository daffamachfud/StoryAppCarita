package com.daffa.storyappcarita.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.daffa.storyappcarita.data.StoryRepository
import com.daffa.storyappcarita.model.response.ListStoryItem
import com.daffa.storyappcarita.model.response.LoginResult
import com.daffa.storyappcarita.util.UserPreference

class MapViewModel(
    private val storyRepository: StoryRepository,
    private val pref: UserPreference
) : ViewModel() {

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun getAllStoriesMap(token:String): LiveData<List<ListStoryItem>> {
        return storyRepository.getAllStoriesMap(token)
    }
}