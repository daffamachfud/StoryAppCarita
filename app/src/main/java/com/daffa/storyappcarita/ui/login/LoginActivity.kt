package com.daffa.storyappcarita.ui.login

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.daffa.storyappcarita.R
import com.daffa.storyappcarita.databinding.ActivityLoginBinding
import com.daffa.storyappcarita.model.LoginResponse
import com.daffa.storyappcarita.model.LoginResult
import com.daffa.storyappcarita.network.ApiConfig
import com.daffa.storyappcarita.ui.MainNewActivity
import com.daffa.storyappcarita.util.UserPreference
import com.daffa.storyappcarita.util.Utils
import com.daffa.storyappcarita.util.ViewModelFactory
import com.daffa.storyappcarita.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initAction()
        initViewModel()
        initAnimation()
    }

    private fun initAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[UserViewModel::class.java]
    }

    private fun initAction() {
        //set button disable ketika data belum terisi
        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)

        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditText.error = "Masukkan Email"
                }
                password.isEmpty() -> {
                    binding.passwordEditText.error = "Masukkan password"
                }
                else -> {
                    Utils.LoadingScreen.displayLoadingWithText(
                        this@LoginActivity,
                        "Tunggu Dulu Yah",
                        false
                    )

                    ApiConfig.getApiService().login(email, password).enqueue(
                        object : Callback<LoginResponse> {
                            override fun onResponse(
                                call: Call<LoginResponse>,
                                response: Response<LoginResponse>
                            ) {
                                Utils.LoadingScreen.hideLoading()
                                if (response.isSuccessful) {
                                    //simpan data prefs
                                    response.body()?.loginResult?.apply {
                                        saveUserResponse(userId, name, token)
                                    }
                                    val intent =
                                        Intent(this@LoginActivity, MainNewActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity, "Gagal login!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(
                                call: Call<LoginResponse>,
                                t: Throwable
                            ) {
                                Utils.LoadingScreen.hideLoading()
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Gagal login    !",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }
                    )
                }
            }

        }
    }

    private fun saveUserResponse(userId: String?, name: String?, token: String?) {
        viewModel.saveUser(LoginResult(name, userId, token))
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

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            checkFieldIsNotEmpty()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private fun checkFieldIsNotEmpty() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        when{
            email.isEmpty() -> {
                binding.btnLogin.isEnabled = false
            }
            password.isEmpty() -> {
                binding.btnLogin.isEnabled = false
            }
            password.length < 6 -> {
                binding.btnLogin.isEnabled = false
            }
            else -> {
                binding.btnLogin.isEnabled = true
            }
        }
    }

}