package com.doctor.ensomnia.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.doctor.ensomnia.R
import com.doctor.ensomnia.data.pojo.Doctor
import com.doctor.ensomnia.databinding.FragmentSignupBinding
import com.doctor.ensomnia.utilites.SupportFunctions
import com.doctor.ensomnia.viewmodel.AuthViewModel
import com.doctor.ensomnia.viewmodel.ViewModelFactory


class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri?=null
    private val authViewModel by lazy {
        val homeViewModelProvider = ViewModelFactory(requireActivity().application)
        ViewModelProvider(this,homeViewModelProvider)[AuthViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentSignupBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setOnClickListeners()
        observeUser()
        return binding.root
    }


    private fun setOnClickListeners(){
        binding.docIv.setOnClickListener {
            getDoctorImage()
        }
        binding.signUpBtn.setOnClickListener {
            signUp()
        }
    }

    private fun isValidateDetails():Boolean {
        if (binding.editNameSignUp.text!!.toString().trim().isEmpty()) {
            binding.editNameSignUp.error = resources.getString(R.string.enter_name)
            binding.editNameSignUp.requestFocus()
            return false
        }
        else if (binding.editEmailSignUp.text!!.toString().trim().isEmpty()){
            binding.editEmailSignUp.error = resources.getString(R.string.enter_email)
            binding.editEmailSignUp.requestFocus()
            return false
        }
        else if (binding.editNumberSignUp.text!!.toString().trim().isEmpty()){
            binding.editNumberSignUp.error = resources.getString(R.string.enter_phone)
            binding.editNumberSignUp.requestFocus()
            return false
        }
        else if (binding.editNumberSignUp.text.toString().length < 11){
            binding.editNumberSignUp.error = resources.getString(R.string.must_be_11_digits)
            binding.editNumberSignUp.requestFocus()
            return false
        }

        else if (binding.editPassSignUp.text!!.toString().trim().isEmpty()){
            binding.editPassSignUp.error = resources.getString(R.string.enter_password)
            binding.editPassSignUp.requestFocus()
            return false
        }
        else if (binding.editPassSignUp.text!!.toString().length < 8){
            binding.editPassSignUp.error = resources.getString(R.string.less_than_7_digits)
            binding.editPassSignUp.requestFocus()
            return false
        }

        return true
    }

    private val pickImage: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    imageUri = result.data!!.data
                    Glide.with(requireContext()).load(imageUri).centerCrop().into(binding.docIv)
                }
            }
        }

    private fun getDoctorImage(){
        SupportFunctions.getImage(pickImage)
    }

    private fun signUp(){
        if (isValidateDetails()){
            if (SupportFunctions.checkForInternet(requireContext()))
            {
                binding.signUpBtn.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
                val doctor = Doctor()
                doctor.name = binding.editNameSignUp.text.toString()
                doctor.email = binding.editEmailSignUp.text.toString()
                doctor.password = binding.editPassSignUp.text.toString()
                doctor.phoneNumber = binding.editNumberSignUp.text.toString()
                authViewModel.signUp(doctor,this,imageUri)
            }
            else
                SupportFunctions.showNoInternetSnackBar(this)

        }
    }

    private fun observeUser(){
        authViewModel.getCurrentUser().observe(viewLifecycleOwner){
            if (it!=null){
                binding.progressBar.visibility = View.INVISIBLE
                binding.signUpBtn.visibility = View.VISIBLE
                clearEditBox()
                val viewPager = requireActivity().findViewById<ViewPager2>(R.id.fragments_viewPager)
                viewPager.currentItem = 0
            }
            else{
                binding.progressBar.visibility = View.INVISIBLE
                binding.signUpBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun clearEditBox()
    {
      binding.editNameSignUp.text.clear()
        binding.editEmailSignUp.text.clear()
        binding.editPassSignUp.text.clear()
        binding.editNumberSignUp.text.clear()
        Glide.with(requireContext()).load(R.drawable.baseline_add_a_photo_24).centerCrop().into(binding.docIv)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}