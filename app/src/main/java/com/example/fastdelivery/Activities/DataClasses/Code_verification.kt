package com.example.fastdelivery.Activities.DataClasses

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fastdelivery.R
import com.example.fastdelivery.databinding.ActivityCodeVerificationBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class Code_verification : AppCompatActivity() {
    private lateinit var binding:ActivityCodeVerificationBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var code:String
    private lateinit var phone_number:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCodeVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

val verificationId=intent.getStringExtra("verifyID").toString()
        phone_number=intent.getStringExtra("phonenumber").toString()


        // focus on the beginnig of the activity
        binding.edittext1.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)


        // arraylist of the edittexts
        val editTexts = arrayOf(
            binding.edittext1,
            binding.edittext2,
            binding.edittext3,
            binding.edittext4,
            binding.edittext5,
            binding.edittext6,
        )

        binding.verifybtn.setOnClickListener {
            if (isReady()){
                code=""
                for (edittext in editTexts){
                    code=code+edittext.text.toString()
                }
                startverification(verificationId,code)}
        }



        editTexts.forEachIndexed { index, editText ->
            editText.setOnKeyListener { v, keyCode, event -> // this a callback when the hardware keyboard get pressed
                // Check if the key pressed is the delete key (backspace)
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    // Check if the EditText is empty and it's not the first EditText
                    if (editText.text.isEmpty() && index > 0) {
                        // Move focus to the previous EditText
                        editTexts[index - 1].requestFocus()
                        editTexts[index - 1].setSelection( editTexts[index - 1].length())
                        true // Consume the event
                    } else {
                        false // Allow normal behavior otherwise
                    }
                } else {
                    false // For other keys, do nothing special
                }
            }
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    Log.d("edit","this shit has been called ${editText.text}")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrEmpty() && s.length == 1) {


                //        editText.removeTextChangedListener(this) // this is to stop the recurcivite of the  editText.setText("")

                        editText.removeTextChangedListener(this)
                      editText.setText("")

                      moveToFirstEmptyEditText(s.toString(), index)
                        Log.d("edit","to see how the remove works ${s.toString()}")

                        // Reattach the listener after the text change
                        editText.addTextChangedListener(this)

                }else {



                }


                }
                private fun moveToFirstEmptyEditText(input: String,index:Int) {
                    val firstEmptyIndex = editTexts.indexOfFirst { it.text.isNullOrEmpty() }
                    if (firstEmptyIndex != -1) {
                        if (firstEmptyIndex!=index){
                            Log.d("edit","this get called 223")
                            // clean the edit text cuz it's not the first how it is empty
                            // it continue without go to the callback functions of textwatcher because we put  editText.removeTextChangedListener(this)

                            editTexts[firstEmptyIndex].setText(input)


                        } else { //it works perfectly with normal case
                            Log.d("edit","this get called 22")
                            editText.setText(input)
                            moveToNextEditText(index)
                        }

                        // this is for the focus

                    }
                }
                // this for the focus
                private fun moveToNextEditText(currentIndex: Int) {

                    if (currentIndex+1 < editTexts.size) {
                        editTexts[currentIndex+1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }



    }

    private fun isReady(): Boolean {
return true
    }

    private fun startverification(verificationid:String,code:String) {
        val credential = PhoneAuthProvider.getCredential(verificationid!!, code)
        signInWithPhoneAuthCredential(credential)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("sign", "signInWithCredential:success")

                    val user = auth.currentUser
                    val db=FirebaseFirestore.getInstance()
                    val User= hashMapOf(
                        "phone_number" to phone_number
                    )
                    Log.d("test",User.toString())
                    if (user != null) {
                        db.collection("Users").document(user.uid).set(User).addOnSuccessListener { Log.d("SignupActivity", "User info saved successfully") }
                            .addOnFailureListener{e -> Log.w("SignupActivity", "Error adding user info", e)}
                    }
                    val intent= Intent(this,Navigation_Activity::class.java)
                    startActivity(intent)
                    finish()
                    Log.d("yeah","yeaaaaaah every thing is good")
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("sign", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.w("sign", "FirebaseAuthInvalidCredentialsException:failure", task.exception)
                    }
                    // Update UI
                }
            }
    }


}