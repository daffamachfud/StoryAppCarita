package com.daffa.storyappcarita.view.register

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.daffa.storyappcarita.R
import com.daffa.storyappcarita.databinding.ActivityRegisterBinding
import com.daffa.storyappcarita.model.ServiceResponse
import com.daffa.storyappcarita.network.ApiConfig
import com.daffa.storyappcarita.util.Utils
import com.daffa.storyappcarita.view.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initAction()
        initAnimation()
    }

    private fun initAnimation() {
        ObjectAnimator.ofFloat(binding.imgRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun initAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = "Masukkan email"
                }
                email.isEmpty() -> {
                    binding.emailTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditText.error = "Masukkan password"
                }
                else -> {
                    Utils.LoadingScreen.displayLoadingWithText(
                        this@RegisterActivity,
                        "Tunggu Dulu Yah",
                        false
                    )
                    ApiConfig.getApiService().register(name, email, password).enqueue(
                        object : Callback<ServiceResponse> {
                            override fun onResponse(
                                call: Call<ServiceResponse>,
                                response: Response<ServiceResponse>
                            ) {
                                Utils.LoadingScreen.hideLoading()
                                if (response.isSuccessful) {
                                    //finish, masukan ke halaman login
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Akun anda telah dibuat, silahkan lakukan login!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent =
                                        Intent(this@RegisterActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Gagal membuat akun!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(
                                call: Call<ServiceResponse>,
                                t: Throwable
                            ) {
                                Utils.LoadingScreen.hideLoading()
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Gagal membuat akun!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }
                    )
                }
            }
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

        binding.passwordEditText.background =
            ContextCompat.getDrawable(this, R.drawable.bg_round_edit)
        binding.passwordEditText.hint = "Masukan Password"
        binding.passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.passwordEditText.textSize = 17f
        binding.passwordEditText.setHintTextColor(ContextCompat.getColor(this, R.color.grey_1))
    }
}