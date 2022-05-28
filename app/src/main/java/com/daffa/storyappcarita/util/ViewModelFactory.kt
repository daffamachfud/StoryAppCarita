package com.daffa.storyappcarita.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daffa.storyappcarita.di.Injection
import com.daffa.storyappcarita.ui.list.ListViewModel
import com.daffa.storyappcarita.ui.login.UserViewModel
import com.daffa.storyappcarita.ui.main.MainViewModel
import com.daffa.storyappcarita.ui.map.MapViewModel

class ViewModelFactory(private val pref: UserPreference, private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(Injection.provideRepository(context) ,pref) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ListViewModel::class.java) -> {
                ListViewModel(Injection.provideRepository(context) ,pref) as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel(Injection.provideRepository(context) ,pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}