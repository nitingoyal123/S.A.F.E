package com.example.safe.splashscreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.safe.Main2Activity
import com.example.safe.R
import com.example.safe.databinding.ActivitySplashScreenBinding
import com.example.safe.loginandsignup.LoginActivity
import com.example.safe.manager.ContactManager
import com.example.safe.manager.MessageManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreenActivity : AppCompatActivity() {
    private val PERMISSION_CODE_MULTIPLE = 1002

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("Progress", "Entered splash screen")
        FirebaseApp.initializeApp(this@SplashScreenActivity)
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this@SplashScreenActivity, R.layout.activity_splash_screen)

        // Check permissions when the activity is created
        checkPermissions()
    }

    private fun checkPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // Check READ_CONTACTS permission
        if (!isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            permissionsToRequest.add(Manifest.permission.READ_CONTACTS)
        }

        // Check READ_SMS permission
        if (!isPermissionGranted(Manifest.permission.READ_SMS)) {
            permissionsToRequest.add(Manifest.permission.READ_SMS)
        }

        // Check RECEIVE_SMS permission
        if (!isPermissionGranted(Manifest.permission.RECEIVE_SMS)) {
            permissionsToRequest.add(Manifest.permission.RECEIVE_SMS)
        }

        // Check POST_NOTIFICATIONS permission (required from Android 13 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Request all permissions if there are any missing
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_CODE_MULTIPLE
            )
        } else {
            proceedToNextActivity() // Will only proceed if all permissions are granted
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun proceedToNextActivity() {
        loadDataAndProceed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE_MULTIPLE -> {
                // Check if all requested permissions were granted
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    proceedToNextActivity()
                } else {
                    // Re-request permissions if not granted
                    checkPermissions()
                }
            }
        }
    }

     private fun loadDataAndProceed() {
//         binding.progressBar.visibility = View.VISIBLE
         lifecycleScope.launch {
             try {
                 if (MessageManager.listOfMessageTables.isEmpty()) {
                     Log.d("Permissions", "READ_CONTACTS granted: ${PackageManager.PERMISSION_GRANTED}")

                     withContext(Dispatchers.IO) {
                         ContactManager.loadContacts(this@SplashScreenActivity)
                         MessageManager.readMessages(this@SplashScreenActivity)
                     }
                 }
                 withContext(Dispatchers.Main) {
                     startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                     finish()
                 }
             } catch (e: Exception) {
                 Log.e("LOAD_DATA_ERROR", e.message.toString())
                 withContext(Dispatchers.Main) {
                     Toast.makeText(this@SplashScreenActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
//                        binding.progressBar.visibility = View.INVISIBLE
                 }
             }
         }
     }
}