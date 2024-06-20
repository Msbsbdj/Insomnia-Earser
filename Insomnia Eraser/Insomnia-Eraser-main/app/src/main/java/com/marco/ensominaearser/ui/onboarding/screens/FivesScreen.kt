package com.marco.ensominaearser.ui.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentFivesScreenBinding

import com.marco.ensominaearser.utilites.Constants
import com.marco.ensominaearser.utilites.PreferenceManager


class FivesScreen : Fragment() {
    private lateinit var binding: FragmentFivesScreenBinding
    private lateinit var preference: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFivesScreenBinding.inflate(layoutInflater)
        preference = PreferenceManager(requireContext())

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.finishBtn.setOnClickListener {
            preference.putBoolean(Constants.ONBOARDING_STATE,true)

            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
        }
        return binding.root
    }




}