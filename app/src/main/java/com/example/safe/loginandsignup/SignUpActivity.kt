package com.example.safe.loginandsignup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.safe.R
import com.example.safe.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    //binding
    private lateinit var binding : ActivitySignUpBinding

    //firebase auth
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this);
        binding = DataBindingUtil.setContentView(this@SignUpActivity, R.layout.activity_sign_up)

        var email = ""
        var password = ""
        var confirmPassword = ""

        binding.apply {

            btnNext.setOnClickListener {

                email = edtSignUpEmail.text.toString()
                password = edtSignUpPassword.text.toString()
                confirmPassword = edtSignUpConfirmPassword.text.toString()

                signUp(email,password,confirmPassword)
            }

        }
    }

    private fun signUp(email: String, password: String, confirmPassword: String) {

        if (password.length < 6) {
            Toast.makeText(this@SignUpActivity,"At least 6 Characters are required for password !!",Toast.LENGTH_SHORT).show()
            return
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this@SignUpActivity,"Confirm Password should be equal to password !!",Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                Log.d("TAGY", "User created")
                if (it.isSuccessful) {
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }.addOnFailureListener {
                Log.d("SIGNUP_ERROR",it.message.toString())
                Toast.makeText(this@SignUpActivity,"Authentication failed !!",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
    }
}