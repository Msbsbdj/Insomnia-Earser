package com.marco.ensominaearser.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.marco.ensominaearser.data.pojo.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LibraryRepository {
    private val mFireStore = FirebaseFirestore.getInstance()
    private var mBooksArrayList: MutableStateFlow<ArrayList<Book>> =
        MutableStateFlow(ArrayList())
    suspend fun getLibraryBooks():MutableStateFlow<ArrayList<Book>>{
        mFireStore.collection("Library").get().addOnSuccessListener {
            val bookList: ArrayList<Book> = ArrayList()
            for (bookObject in it.documents) {
                val book = bookObject.toObject(Book::class.java)
                Log.d("book",book!!.name)
                bookList.add(book)
            }
            CoroutineScope(Dispatchers.IO).launch{
                mBooksArrayList.emit(bookList) }
        }
        return mBooksArrayList
    }

}