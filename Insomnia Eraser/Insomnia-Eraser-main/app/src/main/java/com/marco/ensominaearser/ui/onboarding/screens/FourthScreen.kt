package com.marco.ensominaearser.ui.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentFourthScreenBinding


class FourthScreen : Fragment() {
    private lateinit var binding: FragmentFourthScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFourthScreenBinding.inflate(layoutInflater)
        setListeners()

        return binding.root
    }
    private fun setListeners() {
        binding.nextBtn.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.fragment_viewPager)
            viewPager.currentItem = 4
        }
    }


}