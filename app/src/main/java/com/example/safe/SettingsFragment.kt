package com.example.safe

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.safe.helpcenter.HelpCenterActivity

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_settings, container, false)
        val image = view.findViewById<ImageView>(R.id.profileImage)
        var helpCenter = view.findViewById<ConstraintLayout>(R.id.constraintLayoutHelpCenter)

        image.setOnClickListener{
            toMain(it)
        }

        helpCenter.setOnClickListener {
            toHelpCenter()
        }

        return view
    }

    private fun toHelpCenter() {
        startActivity(Intent(requireContext(), HelpCenterActivity::class.java))
    }

    fun toMain(view : View) {
        startActivity(Intent(requireContext(),MainActivity::class.java))
    }
}