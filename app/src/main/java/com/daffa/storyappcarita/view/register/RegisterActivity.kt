package com.daffa.storyappcarita.view.register

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.daffa.storyappcarita.R
import com.daffa.storyappcarita.databinding.ActivityRegisterBinding
import com.daffa.storyappcarita.model.UserModel
import com.daffa.storyappcarita.model.UserPreference
import com.daffa.storyappcarita.network.ApiService
import com.daffa.storyappcarita.util.Utils
import com.daffa.storyappcarita.util.ViewModelFactory
import com.daffa.storyappcarita.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initAction()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]
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
                    Utils.LoadingScreen.displayLoadingWithText(this@RegisterActivity,"Tunggu Dulu Yah",false)
                    ApiService.ApiConfig().getApiService().register(name, email, password).enqueue(
                        object: Callback<ApiService.ResponseService>{
                            override fun onResponse(
                                call: Call<ApiService.ResponseService>,
                                response: Response<ApiService.ResponseService>
                            ) {
                                Utils.LoadingScreen.hideLoading()
                                if(response.isSuccessful){
                                    viewModel.saveUser(UserModel(
                                        name,email,password,true
                                    ))
                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }else{

                                }
                            }

                            override fun onFailure(
                                call: Call<ApiService.ResponseService>,
                                t: Throwable
                            ) {
                                Utils.LoadingScreen.hideLoading()

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