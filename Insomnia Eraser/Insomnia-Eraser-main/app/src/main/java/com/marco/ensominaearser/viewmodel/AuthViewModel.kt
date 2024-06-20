package com.marco.ensominaearser.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.marco.ensominaearser.data.pojo.User
import com.marco.ensominaearser.repository.AuthRepository

class AuthViewModel(private var application: Application):ViewModel() {

    private var authRepository = AuthRepository(application)


    fun getCurrentUser(): MutableLiveData<FirebaseUser> {
        return authRepository.getCurrentUser()
    }

    fun signUp(user: User) {
        authRepository.signup(user)
    }

    fun signIn(email: String, pass: String) {
        authRepository.login(email, pass)
    }

}