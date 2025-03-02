package com.example.fastdelivery.Activities.DataClasses

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fastdelivery.Models.DataClasses.User
import com.example.fastdelivery.databinding.ActivityEditProfileBinding

class Edit_Profile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var user: User
    private val READ_EXTERNAL_STORAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityEditProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)


        user = intent.getParcelableExtra("user_info")!!
        binding.fullnamenputedittext.setText(user?.full_name ?: "")
        binding.emailinputedittext.setText(user.email)
        binding.phonenumberinputedittext.setText(user.phone_number)
        binding.bioinputedittext.setText(user.bio)

        binding.saveEditBtn.setOnClickListener {
          val  saveduser=User(binding.fullnamenputedittext.text.toString(),binding.emailinputedittext.text.toString(),binding.phonenumberinputedittext.text.toString(),binding.bioinputedittext.text.toString())
            if (user != null && saveduser != user){
            val resultIntent = Intent()
            Log.d("the setresult",user.toString())
            resultIntent.putExtra("user_set_info", saveduser)
            setResult(Activity.RESULT_OK, resultIntent)}
            finish()
        }



        binding.editPhotoProfileBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_REQUEST)
            } else {
                openImagePicker()
            }
        }


    }
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                // Set the selected image to the ImageView
                binding.photoProfile.setImageURI(uri)
            }
        } else {
            Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openImagePicker()
            } else {
                // Permission denied, show a message to the user
            }
        }
    }

}
