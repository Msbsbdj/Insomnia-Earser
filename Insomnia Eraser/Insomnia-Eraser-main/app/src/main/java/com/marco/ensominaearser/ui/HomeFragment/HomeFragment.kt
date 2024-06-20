package com.marco.ensominaearser.ui.HomeFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentHomeBinding
import com.marco.ensominaearser.utilites.PreferenceManager


class HomeFragment : Fragment() {
private lateinit var binding:FragmentHomeBinding
private lateinit var preference:PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        preference = PreferenceManager(requireContext())

        setListeners()
        return binding.root
    }

    private fun setListeners(){
        binding.libraryBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_libraryFragment)
        }
        binding.cvMeditation.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_meditationFragment)
        }
        binding.cvMedicalConsultant.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_medicalConsultantFragment)
        }
        binding.cvHealthyFood.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_healthyFoodFragment)
        }
        binding.cvChat.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mainChatFragment)
        }
    }


}