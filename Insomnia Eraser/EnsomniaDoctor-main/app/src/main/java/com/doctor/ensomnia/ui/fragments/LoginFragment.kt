package com.doctor.ensomnia.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.doctor.ensomnia.R
import com.doctor.ensomnia.ui.RecentChatActivity
import com.doctor.ensomnia.databinding.FragmentLoginBinding
import com.doctor.ensomnia.utilites.PreferenceManager
import com.doctor.ensomnia.utilites.SupportFunctions
import com.doctor.ensomnia.viewmodel.AuthViewModel
import com.doctor.ensomnia.viewmodel.ViewModelFactory


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    private val authViewModel by lazy {
        val homeViewModelProvider = ViewModelFactory(requireActivity().application)
        ViewModelProvider(this,homeViewModelProvider)[AuthViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        preferenceManager = PreferenceManager(requireContext())
        if (preferenceManager.getBoolean("Login")){
            goToRecentChatActivity()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setListeners()
        observeUser()
        return binding.root
    }
private fun setListeners(){

    binding.signInBtn.setOnClickListener {
        login()
    }
}

    private fun login(){
        if (isValidateDetails()){
            if (SupportFunctions.checkForInternet(requireContext())){
                binding.signInBtn.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
                authViewModel.signIn(binding.editEmailSignIN.text.toString(),
                    binding.editPassSignIn.text.toString())
            }
            else{
                SupportFunctions.showNoInternetSnackBar(this)
            }

        }
    }

    private fun isValidateDetails():Boolean {
        if (binding.editEmailSignIN.text!!.toString().trim().isEmpty()) {
            binding.editEmailSignIN.error = resources.getString(R.string.enter_email)
            binding.editEmailSignIN.requestFocus()
            return false
        }
        else if (binding.editPassSignIn.text!!.toString().trim().isEmpty()) {
            binding.editPassSignIn.error = resources.getString(R.string.enter_email)
            binding.editPassSignIn.requestFocus()
            return false
        }

        return true
    }

    private fun observeUser(){
        authViewModel.getCurrentUser().observe(viewLifecycleOwner){
            if (it!=null){
                binding.progressBar.visibility = View.INVISIBLE
                binding.signInBtn.visibility = View.VISIBLE
                goToRecentChatActivity()
            }
            else{
                binding.progressBar.visibility = View.INVISIBLE
                binding.signInBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun goToRecentChatActivity(){
        val intent = Intent(requireContext(), RecentChatActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

}