package com.doctor.ensomnia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctor.ensomnia.data.pojo.Doctor
import com.doctor.ensomnia.repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(private var application: Application):ViewModel() {

    private var authRepository = AuthRepository(application)


    fun getCurrentUser(): MutableLiveData<FirebaseUser> {
        return authRepository.getCurrentUser()
    }

    fun signUp(user: Doctor,fragment: Fragment, imageFileUri: Uri?) {
        authRepository.signup(user,fragment,imageFileUri)
    }

    fun signIn(email: String, pass: String) {
        authRepository.login(email, pass)
    }

}