package com.daffa.storyappcarita.view.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daffa.storyappcarita.databinding.ActivityLandingBinding
import com.daffa.storyappcarita.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}