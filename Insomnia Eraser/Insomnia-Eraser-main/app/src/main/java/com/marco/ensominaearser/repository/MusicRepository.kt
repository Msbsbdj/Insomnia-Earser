package com.marco.ensominaearser.repository
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.marco.ensominaearser.data.pojo.Category
import com.marco.ensominaearser.data.pojo.SongModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MusicRepository {
    private val mFireStore = FirebaseFirestore.getInstance()
    private var mCategoriesArrayList: MutableStateFlow<ArrayList<Category>> =
        MutableStateFlow(ArrayList())

    private var mSongsList: MutableStateFlow<SongModel> =
        MutableStateFlow(SongModel())
    suspend fun getMusicCategories():MutableStateFlow<ArrayList<Category>>{
        mFireStore.collection("category").get().addOnSuccessListener {
            val categoryList: ArrayList<Category> = ArrayList()
            for (categoryObject in it.documents) {
                val category = categoryObject.toObject(Category::class.java)
                if (category != null) {
                    categoryList.add(category)
                }
            }
            CoroutineScope(Dispatchers.IO).launch{
                mCategoriesArrayList.emit(categoryList) }
        }
        return mCategoriesArrayList
    }
    suspend fun getSongs(songsIdList:String):MutableStateFlow<SongModel>{
        mFireStore.collection("songs").document(songsIdList).get().addOnSuccessListener {
            val song = it.toObject(SongModel::class.java)
            CoroutineScope(Dispatchers.IO).launch{
                if (song != null) {
                    Log.d("id",song.id)
                    mSongsList.emit(song)
                }
            }
        }.addOnFailureListener {
            Log.d("Error",it.message.toString())
        }
        return mSongsList
    }

}