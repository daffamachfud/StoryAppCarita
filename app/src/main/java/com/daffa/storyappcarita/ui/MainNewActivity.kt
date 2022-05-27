package com.daffa.storyappcarita.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.daffa.storyappcarita.R
import com.daffa.storyappcarita.databinding.ActivityMainNewBinding
import com.daffa.storyappcarita.ui.add.StoryAddActivity
import com.daffa.storyappcarita.ui.landing.LandingActivity
import com.daffa.storyappcarita.ui.main.MainViewModel
import com.daffa.storyappcarita.util.UserPreference
import com.daffa.storyappcarita.util.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainNewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainNewBinding
    private lateinit var mainViewModel: MainViewModel
    private var tokenIntent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main_new)
        navView.setupWithNavController(navController)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        initViewModel()
        initAction()
    }

    private fun initAction() {
        binding.btnAddStories.setOnClickListener {
            val intent = Intent(this@MainNewActivity, StoryAddActivity::class.java)
            intent.putExtra(StoryAddActivity.TOKEN, tokenIntent)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[MainViewModel::class.java]
        mainViewModel.getUser().observe(this) {
            if (it.token?.isNotEmpty() == true) {
                tokenIntent = it.token
            } else {
                println("onresponse 2")
                startActivity(Intent(this, LandingActivity::class.java))
                finish()
            }
        }

    }
}