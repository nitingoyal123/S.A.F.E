package com.example.safe.helpcenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.safe.R
import com.example.safe.databinding.ActivityContactSupportBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class ContactSupportActivity : AppCompatActivity() {

    private lateinit var binding : ActivityContactSupportBinding

    private lateinit var firestore : FirebaseFirestore

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this@ContactSupportActivity, R.layout.activity_contact_support)
        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth


        binding.btnSubmit.setOnClickListener {
            var ques = binding.etMessage.text.toString()
            if (ques.equals("")) {
                Toast.makeText(this@ContactSupportActivity, "Please enter the question...", Toast.LENGTH_SHORT).show()
            }
            addFAQ(ques, "")
        }
    }
    fun addFAQ(ques : String, ans : String) {
        var map = mapOf(
            "email" to auth.currentUser?.email,
            "ques" to ques,
            "ans" to ans
        )
        firestore.collection("FAQs").add(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this@ContactSupportActivity, "Message submitted successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.d("FAQ ERROR", it.toString())
                Toast.makeText(this@ContactSupportActivity, "Something went wrong !!!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Log.d("FAQ ERROR", it.toString())
            Toast.makeText(this@ContactSupportActivity, "Something went wrong !!!", Toast.LENGTH_SHORT).show()
        }
    }
}