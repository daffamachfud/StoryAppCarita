package com.daffa.storyappcarita.di

import android.content.Context
import com.daffa.storyappcarita.data.StoryRepository
import com.daffa.storyappcarita.database.StoryDatabase
import com.daffa.storyappcarita.network.ApiConfig
import com.daffa.storyappcarita.util.UserPreference

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}