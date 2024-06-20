package com.doctor.ensomnia.repositories

import android.content.Context
import com.doctor.ensomnia.utilites.Constants
import com.doctor.ensomnia.utilites.PreferenceManager
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class ChatRepository {
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var preferenceManger:PreferenceManager

    suspend fun getDoctorId(context: Context):String{
        preferenceManger = PreferenceManager(context)
        return preferenceManger.getString("doctorId")
    }

    fun listenConversations(
        collectionName: String,
        senderIdKey: String,
        patientIdKey: String,
        receiverIdKey: String,
        eventListener: EventListener<QuerySnapshot>
    ) {
        mFireStore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
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