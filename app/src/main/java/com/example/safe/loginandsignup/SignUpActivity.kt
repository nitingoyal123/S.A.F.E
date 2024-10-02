package com.example.safe.loginandsignup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.safe.R
import com.example.safe.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivitySignUpBinding

    //firebase auth
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("Progress", "Entered signup activity")
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this@SignUpActivity, R.layout.activity_sign_up)

        var email = ""
        var password = ""
        var confirmPassword = ""

        binding.apply {

            btnSignup.setOnClickListener {
                binding.constraintLayout.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
                email = edtSignupEmail.text.toString()
                password = edtSignupPassword.text.toString()
                confirmPassword = edtSignupConfirmPassword.text.toString()

                signUp(email, password, confirmPassword)
            }

        }

        binding.txtLogin.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.googleSignInButton.setOnClickListener {
            binding.constraintLayout.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            signInWithGoogle()
        }

    }

    private fun signUp(email: String, password: String, confirmPassword: String) {

        if (password.length < 6) {
            Toast.makeText(
                this@SignUpActivity, "At least 6 Characters are required for password !!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(
                this@SignUpActivity, "Confirm Password should be equal to password !!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d("LOG_SIGNUP", "User created")
                if (it.isSuccessful) {
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener {
                Log.d("LOG_SIGNUP", it.message.toString())
                binding.constraintLayout.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@SignUpActivity, "User already exist !!", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.w("LOG_GOOGLE_SIGNIN", "signInResult:failed code=" + e.statusCode)
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Log.w("Firebase Auth", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}