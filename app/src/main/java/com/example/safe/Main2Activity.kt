//package com.example.safe
//
//import com.example.safe.databinding.ActivityMain2Binding
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.databinding.DataBindingUtil
//import androidx.fragment.app.Fragment
//import com.example.safe.mediaFragments.MediaFragment
//import com.google.android.material.bottomnavigation.BottomNavigationView
//
//class Main2Activity : AppCompatActivity() {
//    private lateinit var binding : ActivityMain2Binding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this@Main2Activity,R.layout.activity_main2)
//
//        val bottomNavigationView = findViewById<BottomNavigationView>(binding.bottomNavigation.id)
//
//        bottomNavigationView.selectedItemId = R.id.navigation_home
//        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
//
//        supportFragmentManager.beginTransaction().replace(
//            R.id.fragment_container,
//            HomeFragment()
//        ).commit()
//    }
//
//    private val navListener =
//        BottomNavigationView.OnNavigationItemSelectedListener { item ->
//            var selectedFragment: Fragment? = null
//            when (item.itemId) {
//                R.id.navigation_history -> selectedFragment = MediaFragment()
//                R.id.navigation_home -> selectedFragment = HomeFragment()
//                R.id.navigation_settings -> selectedFragment = SettingsFragment()
//            }
//            supportFragmentManager.beginTransaction().replace(
//                R.id.fragment_container,
//                selectedFragment!!
//            ).commit()
//            true
//        }
//}

package com.example.safe

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.safe.databinding.ActivityMain2Binding
import com.example.safe.mediaFragments.MediaFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Main2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)
        Log.d("Progress", "Entered main2 activity")
        val bottomNavigationView = findViewById<BottomNavigationView>(binding.bottomNavigation.id)

        bottomNavigationView.selectedItemId = R.id.navigation_home
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commitNow()
        }
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment: Fragment = when (item.itemId) {
            R.id.navigation_history -> MediaFragment()
            R.id.navigation_settings -> SettingsFragment()
            else -> HomeFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .commitNow()

        true
    }
}
