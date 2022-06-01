package com.daffa.storyappcarita.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.daffa.storyappcarita.model.response.LoginResult
import com.daffa.storyappcarita.util.UserPreference

class MainViewModel(
    private val pref: UserPreference
) :
    ViewModel() {

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

}