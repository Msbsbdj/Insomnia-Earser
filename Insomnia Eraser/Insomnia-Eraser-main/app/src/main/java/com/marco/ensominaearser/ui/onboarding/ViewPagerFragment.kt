package com.marco.ensominaearser.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marco.ensominaearser.databinding.FragmentViewPagerBinding


import com.marco.ensominaearser.ui.onboarding.screens.FirstScreen
import com.marco.ensominaearser.ui.onboarding.screens.FivesScreen
import com.marco.ensominaearser.ui.onboarding.screens.FourthScreen
import com.marco.ensominaearser.ui.onboarding.screens.SecondScreen
import com.marco.ensominaearser.ui.onboarding.screens.ThirdScreen


class ViewPagerFragment : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerBinding.inflate(layoutInflater)
        setupViewPagerAdapter()


        return binding.root
    }


    private fun setupViewPagerAdapter(){
        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen(),
            FourthScreen(),
            FivesScreen()
        )
         val viewPagerAdapter = ViewPagerAdapter(fragmentList,
             requireActivity().supportFragmentManager,
             lifecycle)
        binding.fragmentViewPager.adapter = viewPagerAdapter
    }


}