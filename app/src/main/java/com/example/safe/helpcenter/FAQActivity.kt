package com.example.safe.helpcenter

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.safe.R
import com.example.safe.databinding.ActivityFaqactivityBinding

class FAQActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFaqactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this@FAQActivity, R.layout.activity_faqactivity)



    }
}