package com.marco.ensominaearser.viewmodel

import androidx.lifecycle.ViewModel
import com.marco.ensominaearser.data.pojo.Book
import com.marco.ensominaearser.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow

class LibraryViewModel:ViewModel() {
    private var libraryRepository = LibraryRepository()

    suspend fun getLibraryBooks():MutableStateFlow<ArrayList<Book>>{
       return libraryRepository.getLibraryBooks()
    }
}