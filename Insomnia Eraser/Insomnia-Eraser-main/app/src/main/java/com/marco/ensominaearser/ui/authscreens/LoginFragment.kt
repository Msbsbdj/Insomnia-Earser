package com.marco.ensominaearser.ui.authscreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentLoginBinding
import com.marco.ensominaearser.utilites.SupportFunctions
import com.marco.ensominaearser.viewmodel.AuthViewModel
import com.marco.ensominaearser.viewmodel.ViewModelFactory



class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val authViewModel by lazy {
        val homeViewModelProvider = ViewModelFactory(requireActivity().application)
        ViewModelProvider(this,homeViewModelProvider)[AuthViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setListeners()
        return binding.root
    }
    private fun setListeners(){
        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        binding.loginBtn.setOnClickListener {
            if (SupportFunctions.checkForInternet(requireContext()))
            login()
            else
                SupportFunctions.showNoInternetSnackBar(this)
        }
    }

    private fun login(){
        if (isValidateDetails()){
            binding.progressBar.visibility = View.VISIBLE
            binding.loginBtn.visibility = View.INVISIBLE
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            authViewModel.signIn(email,password)
        }
        authViewModel.getCurrentUser().observe(requireActivity()){
            if (it!=null)
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            else
                binding.loginBtn.visibility =View.VISIBLE
        }

    }

    private fun isValidateDetails():Boolean {
        if (binding.emailEt.text!!.toString().trim().isEmpty()) {
            binding.emailEt.error = resources.getString(R.string.enter_email)
            binding.emailEt.requestFocus()
            return false
        } else if (binding.passwordEt.text!!.toString().trim().isEmpty()) {
            binding.passwordEt.error = resources.getString(R.string.enter_password)
            binding.passwordEt.requestFocus()
            return false
        }
        return true
    }


}