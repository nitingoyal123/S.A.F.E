package com.example.safe.mediaFragments

import com.example.safe.adapters.ViewPagerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.safe.MainActivity
import com.example.safe.R
import com.example.safe.databinding.FragmentMediaBinding
import com.google.android.material.tabs.TabLayout

class MediaFragment : Fragment() {

    private lateinit var binding : FragmentMediaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("KOOO", "media fragment")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_media, container, false)

        val adapter = ViewPagerAdapter(parentFragmentManager)
        adapter.addFragment(VideoFragment(), "Video")
        adapter.addFragment(ImageFragment(), "Image")
        adapter.addFragment(AudioFragment(), "Audio")

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }
}
