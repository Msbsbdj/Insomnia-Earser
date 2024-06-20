package com.doctor.ensomnia.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.doctor.ensomnia.adapters.RecentConversationsAdapter

import com.doctor.ensomnia.data.pojo.ChatMessage
import com.doctor.ensomnia.databinding.ActivityRecentChatBinding
import com.doctor.ensomnia.utilites.Constants
import com.doctor.ensomnia.utilites.PreferenceManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecentChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecentChatBinding
    private lateinit  var preferenceManager: PreferenceManager
    private lateinit var conversions: ArrayList<ChatMessage>
    private lateinit var conversationsAdapter: RecentConversationsAdapter
    private var mdoctorId: String? = null
    val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityRecentChatBinding.inflate(layoutInflater)
        preferenceManager = PreferenceManager(this)
            mdoctorId = preferenceManager.getString("doctorId")
            listenConversation()
        init()
           onConversationClickListener()


        setContentView(binding.root)
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
                preferenceManager.getString("doctorId")
            )
            .addSnapshotListener(eventListener)

        mFireStore.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
            .whereEqualTo(
                Constants.KEY_RECEIVER_ID,
                preferenceManager.getString("doctorId")
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
                        if (preferenceManager.getString("doctorId")
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
                Log.d("size",conversions.size.toString())
                binding.conversationsRecyclerView.smoothScrollToPosition(0)
                binding.conversationsRecyclerView.visibility = View.VISIBLE

            }
        }


    private fun onConversationClickListener(){
        conversationsAdapter.onConversationClick = {

            val intent = Intent(this,ChatActivity::class.java)
            intent.putExtra("user",it)
            startActivity(intent)

        }

    }






}