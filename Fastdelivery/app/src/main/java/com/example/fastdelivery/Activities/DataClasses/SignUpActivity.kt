package com.example.fastdelivery.Activities.DataClasses

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fastdelivery.R
import com.example.fastdelivery.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()


        binding.backbtn.setOnClickListener {
            finish()
        }
        binding.SignUpbtn.setOnClickListener {
          if(isReady())  SignUp(binding.phoneinputlayout.editText?.text.toString())
        }
    }


    private fun isReady(): Boolean {
        // i'm gona see if the user exist on the firestore and check all the input text layout
        return true
    }

    private fun SignUp(phoneNumber:String) {


        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            // if onVerificationCompleted is called this mean it's verified so ne noeed for code otp , "i think it's realy rare to happen"
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                Log.d("code", "its verified , the phone number by the onVerificationCompleted")
                // sign in if there is an auto ....
                signInWithPhoneAuthCredential(credential)
            }

            // if this get called so there is a problem and the code will not be send
            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                Log.d("test","the failed get called")

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            //this function get called when neither onVerificationCompleted onVerificationFailed happen so the send get passed to phone number
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                val storedVerificationId = verificationId
                val resendToken = token
                Log.d("token",resendToken.toString())
                Log.d("token",storedVerificationId.toString())

               // val credential=PhoneAuthProvider.getCredential(verificationId!!,"111111")
                val intent=Intent(this@SignUpActivity,Code_verification::class.java)
                intent.putExtra("verifyID",storedVerificationId)
                intent.putExtra("phonenumber",phoneNumber)
                startActivity(intent)
            }
        }

       val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options) // this is the one how sent the sms code



    }

    // this function sign
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                Log.d("test",task.exception.toString())
                if (task.isSuccessful) {

                    val user = task.result?.user

                   Log.d("test","signin success")

                }

             else{
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}