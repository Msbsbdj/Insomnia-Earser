package com.marco.ensominaearser.repository

import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.internal.Constants
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.marco.ensominaearser.data.pojo.Doctor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatRepository {
    private val mFireStore = FirebaseFirestore.getInstance()
    private val doctorsList:MutableStateFlow<ArrayList<Doctor>> = MutableStateFlow(ArrayList())

     suspend fun getDoctors():MutableStateFlow<ArrayList<Doctor>>{
        mFireStore.collection("Doctors").get().addOnSuccessListener {
            val doctorsList = ArrayList<Doctor>()
            for (i in it.documents){
                val doctor = i.toObject(Doctor::class.java)
                doctor!!.id = i.id
                doctorsList.add(doctor)
            }
            CoroutineScope(Dispatchers.IO).launch {
                this@ChatRepository.doctorsList.emit(doctorsList)
            }

        }
        return doctorsList
    }
    fun listenConversations(
        collectionName: String,
        senderIdKey: String,
        patientIdKey: String,
        receiverIdKey: String,
        eventListener: EventListener<QuerySnapshot>
    ) {
        mFireStore.collection(collectionName)
            .whereEqualTo(
                senderIdKey,
                patientIdKey
            )
            .addSnapshotListener(eventListener)

        mFireStore.collection(collectionName)
            .whereEqualTo(
                receiverIdKey,
                patientIdKey
            )
            .addSnapshotListener(eventListener)
    }
}