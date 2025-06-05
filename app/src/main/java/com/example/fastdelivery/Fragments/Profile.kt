package com.example.fastdelivery.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fastdelivery.Activities.DataClasses.Edit_Profile
import com.example.fastdelivery.Models.DataClasses.User
import com.example.fastdelivery.ViewModels.Navigation_User_VM
import com.example.fastdelivery.databinding.FragmentProfileBinding

class Profile : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var spannable: Spannable
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    val user_model: Navigation_User_VM by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("this ","this get executed again")
        resultLauncher = registerForActivityResult(
            // here i will register the launcher with a contract. A contract defines what type of result you're expecting
            // in my case the type is a startactivity for result
            ActivityResultContracts.StartActivityForResult()
        ) { result -> // this is a callback executed every time the activity do (set result) , and will not executed on the first time
            Log.d("the result", "the result get called")
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("the result.code", "good")
                //here i will Handle the result here
                val data = result.data
                //result.data retrieves the Intent that was sent back from the second activity. This Intent contains any data that was passed using setResult() from the second activity.
                val updatedData = data?.getParcelableExtra<User>("user_set_info")
                user_model.save_new_info_user(updatedData!!)


            } else Log.d("the result.code", "bad")
        }



        spannable = SpannableString(binding.editProfText.text).apply {
            setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
        binding.editProfText.text = spannable

        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val savedUri = sharedPreferences.getString("saved_image_uri", null)
        binding.profileImage.setImageURI(Uri.parse(savedUri))

        binding.editProfText.setOnClickListener {
            val intent= Intent(requireContext(),Edit_Profile::class.java)
            intent.putExtra("user_info", user_model.userInfo.value)
            resultLauncher.launch(intent)
        }

        user_model.userInfo.observe(viewLifecycleOwner){ user ->
            if (user != null) {
                binding.userNameText.text = user.full_name
                binding.userNameText2.text=user.full_name
                binding.userEmail.text = user.email
                binding.userBioText.text=user.bio
                binding.userPhoneNumber.text=user.phone_number
            }
        }




    }

}