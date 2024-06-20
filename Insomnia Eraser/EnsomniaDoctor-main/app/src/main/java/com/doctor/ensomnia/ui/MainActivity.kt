package com.doctor.ensomnia.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.doctor.ensomnia.adapters.AuthViewPagerAdapter
import com.doctor.ensomnia.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AuthViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setupViewPager()
        setContentView(binding.root)

    }

    private fun setupViewPager(){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sign In"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sign Up"));
        val fragmentManager: FragmentManager = supportFragmentManager
        adapter = AuthViewPagerAdapter(fragmentManager, lifecycle)
        binding.fragmentsViewPager.setAdapter(adapter)
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.fragmentsViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding.fragmentsViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })


    }

}