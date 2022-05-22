package com.daffa.storyappcarita.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daffa.storyappcarita.model.UserPreference
import com.daffa.storyappcarita.view.main.MainViewModel

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}