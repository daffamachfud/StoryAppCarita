package com.daffa.storyappcarita.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.daffa.storyappcarita.databinding.ActivityMainBinding
import com.daffa.storyappcarita.util.UserPreference
import com.daffa.storyappcarita.util.ViewModelFactory
import com.daffa.storyappcarita.view.add.StoryAddActivity
import com.daffa.storyappcarita.view.landing.LandingActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val storiesAdapter = StoriesAdapter()
    private var tokenIntent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initAction()
        initViewModel()
    }

    private fun initAction() {
        binding.btnLogout.setOnClickListener {
            mainViewModel.logout()
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, StoryAddActivity::class.java)
            intent.putExtra(StoryAddActivity.TOKEN,tokenIntent)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        binding.loadingMain.visibility = View.VISIBLE
        loadData()

        with(binding.rvStories){
            adapter = storiesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

    }

    private fun initView() {
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
    }

    private fun loadData(){
        mainViewModel.getUser().observe(this) { user ->
            if (user.token?.isNotEmpty() == true) {
                tokenIntent = user.token
                mainViewModel.getStoriesFromServer("Bearer ${user.token}").observe(this) { list->
                    binding.loadingMain.visibility = View.GONE
                    if(list.isNotEmpty()){
                        storiesAdapter.setStoriesList(list)
                        binding.rvStories.visibility = View.VISIBLE
                    }
                }
                binding.tvUsername.text = user.name
            } else {
                startActivity(Intent(this, LandingActivity::class.java))
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
        binding.rvStories.smoothScrollToPosition(0)
    }
}