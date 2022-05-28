package com.daffa.storyappcarita.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.daffa.storyappcarita.model.response.LoginResult
import com.daffa.storyappcarita.util.UserPreference
import kotlinx.coroutines.launch

class SettingViewModel(
    private val pref: UserPreference
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}