package com.daffa.storyappcarita.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.daffa.storyappcarita.model.ListStoryItem
import com.daffa.storyappcarita.model.LoginResult
import com.daffa.storyappcarita.model.StoriesResponse
import com.daffa.storyappcarita.network.ApiConfig
import com.daffa.storyappcarita.util.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference) : ViewModel() {

    private var listStoryItem = MutableLiveData<List<ListStoryItem>>()

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun getStoriesFromServer(token:String): LiveData<List<ListStoryItem>>{
        ApiConfig.getApiService().getStories(token).enqueue(
            object : Callback<StoriesResponse>{
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if(response.isSuccessful){
                        listStoryItem.postValue(response.body()?.listStory)
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    Log.e("Error Get Stories",t.message.toString())
                }

            }
        )
        return listStoryItem
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}