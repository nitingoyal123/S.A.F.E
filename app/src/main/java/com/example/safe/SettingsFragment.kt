package com.example.safe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.safe.helpcenter.HelpCenterActivity
import com.example.safe.loginandsignup.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val helpCenter = view.findViewById<ConstraintLayout>(R.id.constraintLayoutHelpCenter)
        auth = FirebaseAuth.getInstance()
        val email = "team@safeapp.site"
        val subject = "Query regarding S.A.F.E. app"

        helpCenter.setOnClickListener {
            openGmail(email, subject)
        }

        view.findViewById<ConstraintLayout>(R.id.layoutLogout).setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        view.findViewById<ConstraintLayout>(R.id.cLManageBlocklist).setOnClickListener {
            startActivity(Intent(requireContext(), BlackListActivity::class.java))
        }

        return view
    }

    private fun openGmail(email : String, subject : String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("mailto:$email"))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)

        try {
            startActivity(Intent.createChooser(intent, "Send email with..."))
        } catch (e : Exception) {
            Toast.makeText(requireContext(), "No email client installed.", Toast.LENGTH_SHORT).show()
        }

    }
}