package com.marco.ensominaearser.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.marco.ensominaearser.R
import com.marco.ensominaearser.data.pojo.ChatMessage
import com.marco.ensominaearser.databinding.FragmentMainChatBinding
import com.marco.ensominaearser.ui.adapters.RecentConversationsAdapter
import com.marco.ensominaearser.utilites.Constants
import com.marco.ensominaearser.utilites.PreferenceManager


class MainChatFragment : Fragment() {
    private lateinit var binding:FragmentMainChatBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var conversions: ArrayList<ChatMessage>
    private lateinit var conversationsAdapter: RecentConversationsAdapter
    val mFireStore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMainChatBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        preferenceManager = PreferenceManager(requireContext())
           listenConversation()
        init()
         setOnClickListeners()
        return binding.root
    }

    private fun init() {
        conversions = ArrayList()
        conversationsAdapter = RecentConversationsAdapter(conversions)
        binding.conversationsRecyclerView.adapter = conversationsAdapter
    }

    private fun listenConversation(){
            mFireStore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(
                    Constants.KEY_SENDER_ID,
                    preferenceManager.getString("PatientId")
                )
                .addSnapshotListener(eventListener)

            mFireStore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(
                    Constants.KEY_RECEIVER_ID,
                    preferenceManager.getString("PatientId")
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
                for (documentChange: DocumentChange in value.documentChanges) {
                    if (documentChange.type == DocumentChange.Type.ADDED) {
                        val senderId: String =
                            documentChange.document.getString(Constants.KEY_SENDER_ID)!!
                        val receiverId: String =
                            documentChange.document.getString(Constants.KEY_RECEIVER_ID)!!
                        val chatMessage = ChatMessage()
                        chatMessage.senderId = senderId
                        chatMessage.receiverId = receiverId
                        if (preferenceManager.getString(Constants.KEY_PATIENT_ID)
                                .equals(senderId)
                        ) {
                            chatMessage.conversionImage =
                                documentChange.document.getString(Constants.KEY_RECEIVER_IMAGE)
                            chatMessage.conversionName =
                                documentChange.document.getString(Constants.KEY_RECEIVER_NAME)
                            chatMessage.conversionId =
                                documentChange.document.getString(Constants.KEY_RECEIVER_ID)
                        } else {
                            chatMessage.conversionImage =
                                documentChange.document.getString(Constants.KEY_SENDER_IMAGE)
                            chatMessage.conversionName =
                                documentChange.document.getString(Constants.KEY_SENDER_NAME)
                            chatMessage.conversionId =
                                documentChange.document.getString(Constants.KEY_SENDER_ID)
                        }
                        chatMessage.message =
                            documentChange.document.getString(Constants.KEY_LAST_MESSAGE)
                        chatMessage.dateObject =
                            documentChange.document.getDate(Constants.KEY_TIMESTAMP)
                        conversions.add(chatMessage)
                    } else if (documentChange.type == DocumentChange.Type.MODIFIED) {
                        for (i in 0..conversions.size) {
                            val senderId: String =
                                documentChange.document.getString(Constants.KEY_SENDER_ID)!!
                            val receiverId: String =
                                documentChange.document.getString(Constants.KEY_RECEIVER_ID)!!
                            if (conversions[i].senderId.equals(senderId) && conversions[i].receiverId.equals(
                                    receiverId
                                )
                            ) {
                                conversions[i].message =
                                    documentChange.document.getString(Constants.KEY_LAST_MESSAGE)
                                conversions[i].dateObject =
                                    documentChange.document.getDate(Constants.KEY_TIMESTAMP)
                                break
                            }
                        }
                    }
                }
                conversions.sortWith { obj1: ChatMessage, obj2: ChatMessage ->
                    obj2.dateObject!!.compareTo(obj1.dateObject)
                }
                conversationsAdapter.notifyDataSetChanged()
                binding.conversationsRecyclerView.smoothScrollToPosition(0)
                binding.conversationsRecyclerView.visibility = View.VISIBLE
                binding.progressBar.visibility =View.INVISIBLE
            }
        }

    private fun setOnClickListeners(){
        binding.fabNewChat.setOnClickListener {
         findNavController().navigate(R.id.action_mainChatFragment_to_doctorsFragment)
        }
        conversationsAdapter.onDoctorClick = {
            val bundle = Bundle()
            bundle.putSerializable("doctor",it)
            findNavController().navigate(R.id.action_mainChatFragment_to_chatFragment,bundle)
        }
    }
}
