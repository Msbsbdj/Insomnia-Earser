package com.marco.ensominaearser.ui.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentFirstScreenBinding


class FirstScreen : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFirstScreenBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

          setListeners()
        return binding.root
    }
    private fun setListeners(){
        binding.nextBtn.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.fragment_viewPager)
            viewPager.currentItem = 1
        }

    }


}