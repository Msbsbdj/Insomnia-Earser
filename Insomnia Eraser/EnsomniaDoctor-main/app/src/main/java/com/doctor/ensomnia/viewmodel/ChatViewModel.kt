package com.doctor.ensomnia.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.doctor.ensomnia.repositories.ChatRepository

class ChatViewModel:ViewModel() {
    private val chatRepository = ChatRepository()
    suspend fun getDoctorId(context: Context):String{
        return chatRepository.getDoctorId(context)
    }
}