package com.example.safe.splashscreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.safe.Main2Activity
import com.example.safe.R
import com.example.safe.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    var PERMISSION_CODE_1 = 1001

    private lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this@SplashScreenActivity,R.layout.activity_splash_screen)

        Log.d("TAGY","Entered in activity")
        checkPermissions()

    }

    private fun checkPermissions() {
        Toast.makeText(this, "Permissions", Toast.LENGTH_SHORT).show()
        // Check if permissions are not granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Request permissions
            Log.d("TAGY","Requested Permission")
            ActivityCompat.requestPermissions(

                this,
                arrayOf(
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_CONTACTS
                ),
                PERMISSION_CODE_1
            ) // Can use any of the permission codes
        } else {
            Log.d("TAGY","All Permissions granted")
            // All permissions are already granted, proceed with app functionality
            // Implement your app logic here
            startActivity(Intent(this@SplashScreenActivity,Main2Activity::class.java))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE_1) {
            // Check if all permissions are granted
            var allPermissionsGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }

            if (allPermissionsGranted) {
                Log.d("TAGY","All Permissions granted")
                startActivity(Intent(this@SplashScreenActivity,Main2Activity::class.java))
                // All permissions are granted, proceed with app functionality
                // Implement your app logic here
            } else {
                Log.d("TAGY","Permissions not granted")
                // Permissions were not granted, show a toast indicating permissions are necessary
                Toast.makeText(this, "Permissions are necessary to proceed", Toast.LENGTH_SHORT).show()
                // Close the app or handle it appropriately
                finish()
            }
        }
    }

}