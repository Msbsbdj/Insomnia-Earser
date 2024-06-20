package com.marco.ensominaearser.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.grocery.groceryshop.utilites.serializable
import com.marco.ensominaearser.data.pojo.ChatMessage
import com.marco.ensominaearser.data.pojo.Doctor
import com.marco.ensominaearser.databinding.FragmentChatBinding
import com.marco.ensominaearser.ui.adapters.ChatAdapter
import com.marco.ensominaearser.utilites.Constants
import com.marco.ensominaearser.utilites.PreferenceManager
import com.marco.ensominaearser.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects


class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var receiverDoctor: Doctor
    private lateinit var chatMessages: ArrayList<ChatMessage>
    private lateinit var chatAdapter: ChatAdapter
    lateinit var preferenceManager: PreferenceManager
    private var conversionId: String? = null
    private var isReceiverAvailable = false
    val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var documentReference: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentChatBinding.inflate(layoutInflater)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        receiverDoctor = requireArguments().serializable<Doctor>("doctor")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        setListeners()
        listenMessages(
            Constants.KEY_COLLECTION_CHAT,
            Constants.KEY_SENDER_ID,
            preferenceManager.getString(Constants.KEY_PATIENT_ID),
            Constants.KEY_RECEIVER_ID,
            receiverDoctor,
            eventListener
        )

        return binding.root
    }

    private fun init() {
        preferenceManager = PreferenceManager(requireContext())
        chatMessages = ArrayList()
        chatAdapter = ChatAdapter(
            preferenceManager.getString(Constants.KEY_PATIENT_ID),
            chatMessages,
            receiverDoctor.image!!
        )
        binding.chatRecyclerView.adapter = chatAdapter
        binding.doctorTextName.text = "Dr ${receiverDoctor.name}"
        Glide.with(requireContext()).load(receiverDoctor.image).into(binding.doctorImage)
    }

        private fun listenMessages(
            collectionName: String,
            senderIdKey: String,
            patientIdKey: String,
            receiverIdKey: String,
            doctor: Doctor,
            eventListener: EventListener<QuerySnapshot>
        ) {
            mFireStore.collection(collectionName)
                .whereEqualTo(
                    senderIdKey,
                    patientIdKey
                )
                .whereEqualTo(receiverIdKey, doctor.id)
                .addSnapshotListener(eventListener)
            mFireStore.collection(collectionName)
                .whereEqualTo(senderIdKey, doctor.id)
                .whereEqualTo(
                    receiverIdKey,
                    patientIdKey
                )
                .addSnapshotListener(eventListener)
        }



    private fun setListeners() {

        binding.layoutSend.setOnClickListener {
            if (binding.inputMessage.text.isNotEmpty()) {
                sendMessage()
            }
        }
    }

    private fun sendMessage() {
        val message: HashMap<Any, Any> = HashMap()
        message[Constants.KEY_SENDER_ID] = preferenceManager.getString(Constants.KEY_PATIENT_ID)
        message[Constants.KEY_RECEIVER_ID] = receiverDoctor.id!!
        message[Constants.KEY_MESSAGE] = binding.inputMessage.text.toString()
        message[Constants.KEY_TIMESTAMP] = Date()
        sendMessageToFireBase(Constants.KEY_COLLECTION_CHAT,
            message,
            conversionId,
            isReceiverAvailable
        )
        binding.inputMessage.text = null
    }


    fun sendMessageToFireBase(
        collectionName: String,
        message: java.util.HashMap<Any, Any>,
        conversionId: String?,
        isReceiverAvailable: Boolean
    ) {

        mFireStore.collection(collectionName).add(message)
        if (conversionId != null) {
            goUpdateConversion()

        } else {
            goAddConversion()

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
    private fun checkForConversion() {
        if (chatMessages.size != 0) {
            checkForConversionRemotely(
                Constants.KEY_COLLECTION_CONVERSATIONS,
                Constants.KEY_SENDER_ID,
                preferenceManager.getString("PatientId"),
                Constants.KEY_RECEIVER_ID,
                receiverDoctor.id!!,
                conversionOnCompleteListener
            )
            checkForConversionRemotely(
                Constants.KEY_COLLECTION_CONVERSATIONS,
                Constants.KEY_RECEIVER_ID,
                receiverDoctor.id!!,
                Constants.KEY_SENDER_ID,
                preferenceManager.getString("PatientId"),
                conversionOnCompleteListener
            )
        }
    }

    fun checkForConversionRemotely(
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
    fun listenAvailabilityOfReceiver(
        collectionName: String,
        labId: String
    ) {
        mFireStore.collection(collectionName).document(
            receiverDoctor.id!!
        ).addSnapshotListener(

        ) { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (value != null) {
               this.receiverAvailable(value)
            }
        }
    }

    fun goAddConversion() {
        val conversion: HashMap<String, Any> = HashMap()
        conversion[Constants.KEY_SENDER_ID] =
            preferenceManager.getString(Constants.KEY_PATIENT_ID)!!
        conversion[Constants.KEY_SENDER_NAME] =
            preferenceManager.getString("PatientFirstName")
        conversion[Constants.KEY_SENDER_IMAGE] = ""
        conversion[Constants.KEY_RECEIVER_ID] = receiverDoctor.id!!
        conversion[Constants.KEY_RECEIVER_NAME] = receiverDoctor.name!!
        conversion[Constants.KEY_RECEIVER_IMAGE] = receiverDoctor.image!!
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
    fun receiverAvailable(value: DocumentSnapshot) {
        if (value.getLong(Constants.KEY_AVAILABILITY) != null) {
            val availability: Int = Objects.requireNonNull(
                value.getLong(Constants.KEY_AVAILABILITY)
            )!!.toInt()
            isReceiverAvailable = availability == 1
        }
        receiverDoctor.fcmToken = value.getString(Constants.KEY_FCM_TOKEN)
        if (receiverDoctor.image == null) {
            receiverDoctor.image = value.getString("image")
            chatAdapter.setReceiverImageProfile(receiverDoctor.image!!)
            chatAdapter.notifyItemChanged(0, chatMessages.size)
        }
        if (isReceiverAvailable) {
            binding.textAvailability.visibility = View.VISIBLE
        } else {
            binding.textAvailability.visibility = View.GONE
        }
    }

    private fun getReadableDateTime(date: Date): String {
        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
    }

    private val conversionOnCompleteListener: OnCompleteListener<QuerySnapshot> =
        OnCompleteListener {
            if (it.isSuccessful && it.result != null && it.result!!.documents.size > 0) {
                val documentSnapshot: DocumentSnapshot = it.result!!.documents[0]
                conversionId = documentSnapshot.id
            }
        }
}