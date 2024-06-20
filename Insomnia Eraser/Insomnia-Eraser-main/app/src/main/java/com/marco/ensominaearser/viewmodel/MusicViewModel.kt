package com.marco.ensominaearser.viewmodel

import androidx.lifecycle.ViewModel
import com.marco.ensominaearser.data.pojo.Category
import com.marco.ensominaearser.data.pojo.SongModel
import com.marco.ensominaearser.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow

class MusicViewModel:ViewModel() {
    private var musicRepository = MusicRepository()
    suspend fun getCategories():MutableStateFlow<ArrayList<Category>>{
        return musicRepository.getMusicCategories()

    }
    suspend fun getSongs(songId:String):MutableStateFlow<SongModel>{
        return musicRepository.getSongs(songId)

    }
}