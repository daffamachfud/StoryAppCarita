package com.daffa.storyappcarita.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daffa.storyappcarita.model.LoginResult
import com.daffa.storyappcarita.util.UserPreference
import kotlinx.coroutines.launch

class UserViewModel(private val pref: UserPreference)  : ViewModel(){

    fun saveUser(user: LoginResult) {
        viewModelScope.launch {
            pref.saveDataUser(user)
        }
    }

}