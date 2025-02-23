package com.example.fastdelivery.Fragments

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fastdelivery.Activities.DataClasses.Edit_Profile
import com.example.fastdelivery.ViewModels.Navigation_User_VM
import com.example.fastdelivery.databinding.FragmentProfileBinding

class Profile : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var spannable: Spannable
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
        spannable = SpannableString(binding.editProfText.text).apply {
            setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)}
        binding.editProfText.text = spannable



        binding.editProfText.setOnClickListener {
            val intent= Intent(requireContext(),Edit_Profile::class.java)
            startActivity(intent)
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