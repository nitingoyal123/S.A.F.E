package com.example.safe.mediaFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.safe.R
import com.example.safe.databinding.FragmentAudioBinding

class AudioFragment : Fragment() {

    private lateinit var binding : FragmentAudioBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_audio, container, false)


        return binding.root
    }
}