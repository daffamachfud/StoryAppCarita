package com.daffa.storyappcarita.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.daffa.storyappcarita.databinding.ActivityStoryAddBinding
import com.daffa.storyappcarita.model.ServiceResponse
import com.daffa.storyappcarita.network.ApiConfig
import com.daffa.storyappcarita.util.Utils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryAddActivity : AppCompatActivity() {

    companion object {
        const val TOKEN = "token"
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private lateinit var binding: ActivityStoryAddBinding
    private var getFile: File? = null
    private var token = ""
    private var isBackCamera = false
    private var isFromGallery = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getExtra()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding
            .btnCamera.setOnClickListener {
                startCamera()
            }

        binding.btnGallery.setOnClickListener {
            openGallery()
        }

        binding.btnPost.setOnClickListener { uploadStory() }
    }

    private fun getExtra() {
        val extras = intent.extras
        if (extras != null) {
            token = extras.getString(TOKEN).toString()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = Utils().uriToFile(selectedImg, this@StoryAddActivity)

            getFile = myFile
            isFromGallery = true

            binding.imgAddStory.setImageURI(selectedImg)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            isFromGallery = false
            getFile = myFile
            val result = Utils().rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.imgAddStory.setImageBitmap(result)
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadStory() {

        val description = binding.etDesc.text.toString()
        when {
            description.isEmpty() -> {
                Toast.makeText(this@StoryAddActivity,"Isi deskripsi dari carita kamu !",Toast.LENGTH_LONG).show()
            }
            getFile == null -> {
                Toast.makeText(this@StoryAddActivity,"Pilih gambar untuk carita kamu !",Toast.LENGTH_LONG).show()
            }
            else -> {
                val file = Utils().reduceFileImage(getFile as File, isBackCamera,isFromGallery)

                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                Utils.LoadingScreen.displayLoadingWithText(
                    this@StoryAddActivity,
                    "Tunggul dulu yah",
                    false
                )
                ApiConfig.getApiService().addStory(
                    "Bearer $token", imageMultipart, description
                ).enqueue(object : Callback<ServiceResponse> {
                    override fun onResponse(
                        call: Call<ServiceResponse>,
                        response: Response<ServiceResponse>
                    ) {
                        if (response.isSuccessful) {
                            Utils.LoadingScreen.hideLoading()
                            Toast.makeText(
                                this@StoryAddActivity,
                                "Story berhasil di upload",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                        Utils.LoadingScreen.hideLoading()
                        Toast.makeText(this@StoryAddActivity, "Gagal upload story", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            }
        }
    }


}