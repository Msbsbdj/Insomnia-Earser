package com.marco.ensominaearser.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ViewModelFactory(private val application: Application):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass == AuthViewModel::class.java) {
            AuthViewModel(application) as T
        } else
            super.create(modelClass);
    }
}