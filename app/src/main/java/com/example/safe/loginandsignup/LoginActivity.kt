package com.example.safe.loginandsignup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.safe.Main2Activity
import com.example.safe.R
import com.example.safe.databinding.ActivityLoginBinding
import com.example.safe.manager.ContactManager
import com.example.safe.manager.MessageManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("Progress", "Entered login activity")
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        if (auth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, Main2Activity::class.java))
        }
        binding.btnLogin.setOnClickListener { login() }
        binding.txtRegsiter.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SignUpActivity::class.java
                )
            )
        }

        binding.txtForgotPassword.setOnClickListener {
            forgotPassword(binding.edtLoginEmail.text.toString())
        }
    }

    private fun forgotPassword(email: String) {
        // Check if the email field is empty
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show()
            return
        }

        // firebase doesn't provide email verification
        // we can use third party libraries to do that, like Hunter.io, NeverBounce, or ZeroBounce
        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
            return
        }

        // Send password reset email using Firebase Auth
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent. Please check your inbox.", Toast.LENGTH_LONG).show()
                    Log.d("LoginActivity", "Password reset email sent to: $email")
                } else {
                    Toast.makeText(this, "Error sending reset email. Please try again later.", Toast.LENGTH_SHORT).show()
                    task.exception?.let { Log.e("LoginActivity", it.message.toString()) }
                }
            }
    }


    fun login() {
        val email = binding.edtLoginEmail.text.toString().trim()
        val password = binding.edtLoginPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                startActivity(Intent(this@LoginActivity, Main2Activity::class.java))
            }
            .addOnFailureListener { exception ->
                Log.e("LOGIN_ERROR", exception.message.toString())
                Toast.makeText(this, "Something went wrong, Try again later..", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}
