package com.marco.ensominaearser.viewmodel

import androidx.lifecycle.ViewModel
import com.marco.ensominaearser.data.pojo.Doctor
import com.marco.ensominaearser.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow

class ChatViewModel:ViewModel() {
    private val chatRepository = ChatRepository()

     suspend fun getDoctors():MutableStateFlow<ArrayList<Doctor>>{
       return chatRepository.getDoctors()
    }
}