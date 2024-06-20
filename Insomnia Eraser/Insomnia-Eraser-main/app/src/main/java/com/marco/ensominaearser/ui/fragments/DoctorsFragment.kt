package com.marco.ensominaearser.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentDoctorsBinding
import com.marco.ensominaearser.ui.adapters.DoctorsAdapter
import com.marco.ensominaearser.viewmodel.ChatViewModel
import com.marco.ensominaearser.viewmodel.MealsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DoctorsFragment : Fragment() {
    private var _binding: FragmentDoctorsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels<ChatViewModel>()
    private lateinit var doctorsAdapter:DoctorsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentDoctorsBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getDoctors()

        return binding.root
    }

    private fun getDoctors(){
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getDoctors().collect{
                Log.d("size",it.size.toString())
                if (it.isNotEmpty()){
                    doctorsAdapter = DoctorsAdapter(it)
                    withContext(Dispatchers.Main){
                        binding.progressBar.visibility = View.INVISIBLE
                        setupDoctorsRecyclerView()
                    }

                }
            }
        }
    }

    private fun setupDoctorsRecyclerView(){
        binding.doctorsRecyclerView.adapter = doctorsAdapter
        binding.doctorsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        onDoctorCardClickListener()
    }


    private fun onDoctorCardClickListener(){
        doctorsAdapter.onDoctorClick = {
            val bundle = Bundle()
            bundle.putSerializable("doctor",it)
            findNavController().navigate(R.id.action_doctorsFragment_to_chatFragment,bundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}