package com.daffa.storyappcarita.view.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daffa.storyappcarita.model.UserModel
import com.daffa.storyappcarita.model.UserPreference
import kotlinx.coroutines.launch

class RegisterViewModel(private val pref: UserPreference)  : ViewModel(){
    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}