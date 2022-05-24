package com.daffa.storyappcarita.view.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.daffa.storyappcarita.databinding.ActivityDetailBinding
import com.daffa.storyappcarita.util.Utils

class DetailActivity : AppCompatActivity() {

    companion object {
        const val DETAIL_NAME = "detail_name"
        const val DETAIL_IMG = "detail_img"
        const val DETAIL_DESC = "detail_desc"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        Utils.LoadingScreen.displayLoadingWithText(this,"Tunggu dulu yah",false)

        val extras = intent.extras
        if (extras != null) {
            Utils.LoadingScreen.hideLoading()
            val name = extras.getString(DETAIL_NAME)
            val desc = extras.getString(DETAIL_DESC)
            val photo = extras.getString(DETAIL_IMG)
            Glide.with(this@DetailActivity)
                .load(photo)
                .into(binding.imageDetail)
            binding.tvName.text = name
            binding.tvDesc.text = desc
        }else{
            Utils.LoadingScreen.hideLoading()
            Toast.makeText(this,"Gagal memuat data !",Toast.LENGTH_LONG).show()
            finish()
        }
    }
}