package com.daffa.storyappcarita.view.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daffa.storyappcarita.databinding.ActivityLandingBinding
import com.daffa.storyappcarita.databinding.ActivityMainBinding

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}