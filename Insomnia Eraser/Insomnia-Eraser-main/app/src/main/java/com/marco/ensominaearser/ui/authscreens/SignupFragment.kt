package com.marco.ensominaearser.ui.authscreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentSignupBinding
import com.marco.ensominaearser.data.pojo.User
import com.marco.ensominaearser.utilites.SupportFunctions
import com.marco.ensominaearser.viewmodel.AuthViewModel
import com.marco.ensominaearser.viewmodel.ViewModelFactory


class SignupFragment : Fragment() {
    private lateinit var binding:FragmentSignupBinding
    private val authViewModel by lazy {
        val homeViewModelProvider = ViewModelFactory(requireActivity().application)
        ViewModelProvider(this,homeViewModelProvider)[AuthViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignupBinding.inflate(layoutInflater)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
     setListeners()
        return binding.root
    }
    private fun setListeners(){
       binding.signupButton.setOnClickListener {
           if (SupportFunctions.checkForInternet(requireContext())){
               signUp()
           } else{
               SupportFunctions.showNoInternetSnackBar(this)
           }

       }
    }

    private fun signUp(){
            if (isValidateDetails()){
                binding.signupButton.visibility = View.INVISIBLE
                binding.progressBar.visibility =View.VISIBLE
                val firstName = binding.firstNameEditText.text.toString()
                val lastName = binding.lastNameEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val phone = binding.phoneEditText.text.toString()
                val gender = if (binding.maleBtn.isChecked)
                    "Male"
                else
                    "Female"
                val user = User("",firstName,lastName,email,password,phone,gender)

                authViewModel.signUp(user)
            }

        authViewModel.getCurrentUser().observe(requireActivity()) {
            if (it!=null)
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            else
                binding.signupButton.visibility = View.VISIBLE
        }

    }



    private fun isValidateDetails():Boolean {
        if (binding.firstNameEditText.text!!.toString().trim().isEmpty()) {
            binding.firstNameEditText.error = resources.getString(R.string.enter_first_name)
            binding.firstNameEditText.requestFocus()
            return false
        }
        else if (binding.lastNameEditText.text!!.toString().trim().isEmpty()){
            binding.lastNameEditText.error = resources.getString(R.string.enter_last_name)
            binding.lastNameEditText.requestFocus()
            return false
        }
        else if (binding.emailEditText.text!!.toString().trim().isEmpty()){
            binding.emailEditText.error = resources.getString(R.string.enter_email)
            binding.emailEditText.requestFocus()
            return false
        }

        else if (binding.passwordEditText.text!!.toString().trim().isEmpty()){
            binding.passwordEditText.error = resources.getString(R.string.enter_password)
            binding.passwordEditText.requestFocus()
            return false
        }
        else if (binding.passwordEditText.text!!.toString().length < 8){
            binding.passwordEditText.error = resources.getString(R.string.must)
            binding.passwordEditText.requestFocus()
            return false
        }
        else if (binding.passwordEditText.text!!.toString() != binding.passwordConfirmEditText.text.toString()) {
            binding.passwordConfirmEditText.error = resources.getString(R.string.not_the_same)
            binding.passwordConfirmEditText.requestFocus()
            return false
        }
        else if (binding.phoneEditText.text.toString().trim().isEmpty())
        {
            binding.phoneEditText.error = resources.getString(R.string.enter_phone)
            binding.phoneEditText.requestFocus()
            return false
        }
        else if (binding.phoneEditText.text.toString().length < 11){
            binding.phoneEditText.error = resources.getString(R.string.number_length)
            binding.phoneEditText.requestFocus()
            return false
        }
        return true
    }


}