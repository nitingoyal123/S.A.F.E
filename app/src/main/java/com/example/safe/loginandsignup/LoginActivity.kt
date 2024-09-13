//package com.example.safe.loginandsignup
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.lifecycleScope
//import com.example.safe.Main2Activity
//import com.example.safe.R
//import com.example.safe.databinding.ActivityLoginBinding
//import com.example.safe.manager.ContactManager
//import com.example.safe.manager.MessageManager
//import com.google.firebase.FirebaseApp
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class LoginActivity : AppCompatActivity() {
//
//    // Firestore
//    private lateinit var db: FirebaseFirestore
//
//    // Binding
//    private lateinit var binding: ActivityLoginBinding
//
//    // Firebase Auth
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        Log.d("kkkk", "helloooooo")
//        FirebaseApp.initializeApp(this)
//        auth = FirebaseAuth.getInstance()
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
//        db = Firebase.firestore
//
//        // OnClickListener for login button
//        binding.btnlogin.setOnClickListener {
//            binding.progressBar.visibility = View.VISIBLE
//            val email = binding.edtLoginEmail.text.toString()
//            val password = binding.edtLoginPassword.text.toString()
//
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        lifecycleScope.launch {
//                            if (MessageManager.listOfMessageTables.isEmpty()) {
//                                ContactManager.loadContacts(this@LoginActivity)
//                                MessageManager.readMessages(this@LoginActivity)
//                            }
//                            withContext(Dispatchers.Main) {
//                                startActivity(Intent(this@LoginActivity, Main2Activity::class.java))
//                            }
//                        }
//                    } else {
//                        Toast.makeText(this, "Invalid Id or password !!", Toast.LENGTH_SHORT).show()
//                        binding.progressBar.visibility = View.INVISIBLE
//                    }
//                }.addOnFailureListener {
//                    Log.d("LOGIN_ERROR", it.message.toString())
//                    Toast.makeText(this, "Something went wrong !!", Toast.LENGTH_SHORT).show()
//                    binding.progressBar.visibility = View.INVISIBLE
//                }
//        }
//
//        binding.txtSignup.setOnClickListener {
//            startActivity(Intent(this, SignUpActivity::class.java))
//        }
//    }
//}
//






//package com.example.safe.loginandsignup
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.lifecycleScope
//import com.example.safe.Main2Activity
//import com.example.safe.R
//import com.example.safe.databinding.ActivityLoginBinding
//import com.example.safe.manager.ContactManager
//import com.example.safe.manager.MessageManager
//import com.google.firebase.FirebaseApp
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class LoginActivity : AppCompatActivity() {
//
//    private lateinit var db: FirebaseFirestore
//    private lateinit var binding: ActivityLoginBinding
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        FirebaseApp.initializeApp(this)
//        auth = FirebaseAuth.getInstance()
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
//        db = Firebase.firestore
//
//        binding.btnlogin.setOnClickListener { handleLogin() }
//        binding.txtSignup.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }
//    }
//
//    private fun handleLogin() {
//        val email = binding.edtLoginEmail.text.toString().trim()
//        val password = binding.edtLoginPassword.text.toString().trim()
//
//        if (email.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        binding.progressBar.visibility = View.VISIBLE
//
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    loadDataAndProceed()
//                } else {
//                    handleLoginFailure()
//                }
//            }.addOnFailureListener { exception ->
//                Log.e("LOGIN_ERROR", exception.message.toString())
//                Toast.makeText(this, "Login failed: ${exception.message}", Toast.LENGTH_SHORT).show()
//                binding.progressBar.visibility = View.INVISIBLE
//            }
//    }
//
//    private fun loadDataAndProceed() {
//        lifecycleScope.launch {
//            try {
//                if (MessageManager.listOfMessageTables.isEmpty()) {
//                    coroutineScope {
//                        ContactManager.loadContacts(this@LoginActivity)
//                        MessageManager.readMessages(this@LoginActivity)
//                    }
//                }
//                withContext(Dispatchers.Main) {
//                    binding.progressBar.visibility = View.INVISIBLE
//                    startActivity(Intent(this@LoginActivity, Main2Activity::class.java))
//                }
//            } catch (e: Exception) {
//                Log.e("LOAD_DATA_ERROR", e.message.toString())
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@LoginActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
//                    binding.progressBar.visibility = View.INVISIBLE
//                }
//            }
//        }
//    }
//
//    private fun handleLoginFailure() {
//        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
//        binding.progressBar.visibility = View.INVISIBLE
//    }
//}


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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        if (auth.currentUser != null) {
            binding.pg2.visibility = View.VISIBLE
            loadDataAndProceed()
        } else {
            db = Firebase.firestore

            binding.btnlogin.setOnClickListener { handleLogin() }
            binding.txtSignup.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }

            // Configure Google Sign-In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)
            binding.googleSignInButton.setOnClickListener { signInWithGoogle() }
        }
    }

    private fun handleLogin() {
        val email = binding.edtLoginEmail.text.toString().trim()
        val password = binding.edtLoginPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadDataAndProceed()
                } else {
                    handleLoginFailure()
                }
            }.addOnFailureListener { exception ->
                Log.e("LOGIN_ERROR", exception.message.toString())
                Toast.makeText(this, "Login failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.INVISIBLE
            }
    }

    private fun handleLoginFailure() {
        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
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
                    loadDataAndProceed()
                } else {
                    Log.w("Firebase Auth", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loadDataAndProceed() {
        lifecycleScope.launch {
            try {
                if (MessageManager.listOfMessageTables.isEmpty()) {
                    withContext(Dispatchers.IO) {
                        ContactManager.loadContacts(this@LoginActivity)
                        MessageManager.readMessages(this@LoginActivity)
                    }
                }
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.INVISIBLE
                    startActivity(Intent(this@LoginActivity, Main2Activity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                Log.e("LOAD_DATA_ERROR", e.message.toString())
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

}
