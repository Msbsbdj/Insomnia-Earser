package com.doctor.ensomnia.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doctor.ensomnia.ui.fragments.LoginFragment
import com.doctor.ensomnia.ui.fragments.SignupFragment


class AuthViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            SignupFragment()
        } else LoginFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}