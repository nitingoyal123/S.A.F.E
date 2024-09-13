package com.example.safe.helpcenter

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.safe.R
import com.example.safe.databinding.ActivityHelpCenterBinding

class HelpCenterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHelpCenterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this@HelpCenterActivity, R.layout.activity_help_center)

        binding.btnFaq.setOnClickListener {
            moveToFAQ()
        }

        binding.btnContactSupport.setOnClickListener {
            moveToContactSupport()
        }

    }

    private fun moveToContactSupport() {
        startActivity(Intent(this@HelpCenterActivity, ContactSupportActivity::class.java))

    }

    private fun moveToFAQ() {
        startActivity(Intent(this@HelpCenterActivity, FAQActivity::class.java))
    }
}