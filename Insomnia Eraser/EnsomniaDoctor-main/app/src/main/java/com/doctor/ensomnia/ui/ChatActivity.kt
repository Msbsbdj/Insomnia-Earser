package com.doctor.ensomnia.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.doctor.ensomnia.R
import com.doctor.ensomnia.adapters.ChatAdapter
import com.doctor.ensomnia.data.pojo.ChatMessage
import com.doctor.ensomnia.data.pojo.User
import com.doctor.ensomnia.databinding.ActivityChatBinding
import com.doctor.ensomnia.utilites.Constants
import com.doctor.ensomnia.utilites.PreferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatBinding
    private lateinit var receiverUser: User
    private lateinit var chatMessages: ArrayList<ChatMessage>
    private lateinit var chatAdapter: ChatAdapter
    lateinit var preferenceManager: PreferenceManager
    private var conversionId: String? = null
    private var isReceiverAvailable = false
    val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var documentReference: DocumentReference
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityChatBinding.inflate(layoutInflater)
        receiverUser = intent.getSerializableExtra("user") as User
        init()
        setListeners()
        listenMessages(
            Constants.KEY_COLLECTION_CHAT,
            Constants.KEY_SENDER_ID,
            preferenceManager.getString("doctorId"),
            Constants.KEY_RECEIVER_ID,
            receiverUser,
            eventListener
        )
        setContentView(binding.root)

    }
    private fun init() {
        preferenceManager = PreferenceManager(this)
        chatMessages = ArrayList()
        chatAdapter = ChatAdapter(
            preferenceManager.getString("doctorId"),
            chatMessages,
          "https://firebasestorage.googleapis.com/v0/b/ensomina-earse.appspot.com/o/user.png?alt=media&token=85a150dc-c73b-4eaa-b1c7-e1903c6ee08d"
        )
        binding.chatRecyclerView.adapter = chatAdapter
        binding.doctorTextName.text = receiverUser.firstName+receiverUser.lastName
        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/ensomina-earse.appspot.com/o/user.png?alt=media&token=85a150dc-c73b-4eaa-b1c7-e1903c6ee08d").into(binding.userImage)
    }

    private fun listenMessages(
        collectionName: String,
        senderIdKey: String,
        patientIdKey: String,
        receiverIdKey: String,
        user: User,
        eventListener: EventListener<QuerySnapshot>
    ) {
        mFireStore.collection(collectionName)
            .whereEqualTo(
                senderIdKey,
                patientIdKey
            )
            .whereEqualTo(receiverIdKey, user.id)
            .addSnapshotListener(eventListener)
        mFireStore.collection(collectionName)
            .whereEqualTo(senderIdKey, user.id)
            .whereEqualTo(
                receiverIdKey,
                patientIdKey
            )
            .addSnapshotListener(eventListener)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val eventListener: EventListener<QuerySnapshot> =
        EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                return@EventListener
            }
            if (value != null) {
                val count = chatMessages.size
                for (documentChange in value.documentChanges) {
                    if (documentChange.type == DocumentChange.Type.ADDED) {
                        val chatMessage = ChatMessage()
                        chatMessage.senderId =
                            documentChange.document.getString(Constants.KEY_SENDER_ID)
                        chatMessage.receiverId =
                            documentChange.document.getString(Constants.KEY_RECEIVER_ID)
                        chatMessage.message =
                            documentChange.document.getString(Constants.KEY_MESSAGE)
                        chatMessage.dateTime =
                            getReadableDateTime(documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!)
                        chatMessage.dateObject =
                            documentChange.document.getDate(Constants.KEY_TIMESTAMP)
                        chatMessages.add(chatMessage)
                    }

                }

                chatMessages.sortWith { obj1: ChatMessage, obj2: ChatMessage ->
                    obj1.dateObject!!.compareTo(obj2.dateObject)
                }
                if (count == 0) {
                    chatAdapter.notifyDataSetChanged()
                } else {
                    chatAdapter.notifyItemRangeInserted(chatMessages.size, chatMessages.size)
                    binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size - 1)
                }
                binding.chatRecyclerView.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.INVISIBLE
            if (conversionId == null) {
                checkForConversion()
            }
        }
    fun sendMessageToFireBase(
        collectionName: String,
        message: java.util.HashMap<Any, Any>,
        conversionId: String?,
        isReceiverAvailable: Boolean
    ) {

        mFireStore.collection(collectionName).add(message)
        if (conversionId != null) {
            Log.d("conversationId",conversionId)
            goUpdateConversion()

        } else {
            goAddConversion()

        }
        if (!isReceiverAvailable) {
            //goSendNotification()
        }
    }

    private fun sendMessage() {
        val message: HashMap<Any, Any> = HashMap()
        message[Constants.KEY_SENDER_ID] = preferenceManager.getString("doctorId")
        message[Constants.KEY_RECEIVER_ID] = receiverUser.id
        message[Constants.KEY_MESSAGE] = binding.inputMessage.text.toString()
        message[Constants.KEY_TIMESTAMP] = Date()

        sendMessageToFireBase(Constants.KEY_COLLECTION_CHAT,
            message,
            conversionId,
            isReceiverAvailable
        )
        binding.inputMessage.text = null
    }

    private fun checkForConversionRemotely(
        collectionName: String,
        senderIdKey: String,
        senderId: String,
        receiverIdKey: String,
        receiverId: String,
        conversionOnCompleteListener: OnCompleteListener<QuerySnapshot>
    ) {
        mFireStore.collection(collectionName)
            .whereEqualTo(senderIdKey, senderId)
            .whereEqualTo(receiverIdKey, receiverId)
            .get()
            .addOnCompleteListener(conversionOnCompleteListener)
    }

    private fun checkForConversion() {
        if (chatMessages.size != 0) {
            checkForConversionRemotely(
                Constants.KEY_COLLECTION_CONVERSATIONS,
                Constants.KEY_SENDER_ID,
                preferenceManager.getString("doctorId"),
                Constants.KEY_RECEIVER_ID,
                receiverUser.id,
                conversionOnCompleteListener
            )
            checkForConversionRemotely(
                Constants.KEY_COLLECTION_CONVERSATIONS,
                Constants.KEY_RECEIVER_ID,
                receiverUser.id,
                Constants.KEY_SENDER_ID,
                preferenceManager.getString("doctorId"),
                conversionOnCompleteListener
            )
        }
    }
    private val conversionOnCompleteListener: OnCompleteListener<QuerySnapshot> =
        OnCompleteListener {
            if (it.isSuccessful && it.result != null && it.result!!.documents.size > 0) {
                val documentSnapshot: DocumentSnapshot = it.result!!.documents[0]
                conversionId = documentSnapshot.id
                Log.d("abdelrahmanCon",conversionId!!)
            }
        }





    fun goAddConversion() {
        val conversion: HashMap<String, Any> = HashMap()
        conversion[Constants.KEY_SENDER_ID] =
            preferenceManager.getString("doctorId")
        conversion[Constants.KEY_SENDER_NAME] =
            preferenceManager.getString("doctorName")
        conversion[Constants.KEY_SENDER_IMAGE] = ""
        conversion[Constants.KEY_RECEIVER_ID] = receiverUser.id!!
        conversion[Constants.KEY_RECEIVER_NAME] = "${receiverUser.firstName} \t ${receiverUser.lastName} "
        conversion[Constants.KEY_RECEIVER_IMAGE] = "https://firebasestorage.googleapis.com/v0/b/ensomina-earse.appspot.com/o/user.png?alt=media&token=85a150dc-c73b-4eaa-b1c7-e1903c6ee08d"
        conversion[Constants.KEY_LAST_MESSAGE] = binding.inputMessage.text.toString()
        conversion[Constants.KEY_TIMESTAMP] = Date()
        addConversion(conversion)
    }
    fun addConversion(
        conversion: java.util.HashMap<String, Any>
    ) {
        mFireStore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
            .add(conversion)
            .addOnSuccessListener {

            }
    }
    fun goUpdateConversion() {
        updateConversion(Constants.KEY_COLLECTION_CONVERSATIONS,
            conversionId!!,
            Constants.KEY_LAST_MESSAGE,
            binding.inputMessage.text.toString(),
            Constants.KEY_TIMESTAMP)
    }

    fun updateConversion(
        collectionName: String,
        conversionId: String,
        lastMessageKey: String,
        message: String,
        timeStampKey: String
    ) {
        documentReference =
            mFireStore.collection(collectionName).document(conversionId)
        documentReference.update(
            lastMessageKey,
            message,
            timeStampKey,
            Date()
        )
    }
    private fun setListeners() {

        binding.layoutSend.setOnClickListener {
            if (binding.inputMessage.text.isNotEmpty()) {
                sendMessage()
            }
        }
    }


    private fun getReadableDateTime(date: Date): String {
        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
    }
}