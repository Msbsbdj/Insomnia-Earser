package com.doctor.ensomnia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doctor.ensomnia.R
import com.doctor.ensomnia.data.pojo.ChatMessage
import com.doctor.ensomnia.data.pojo.User
import com.doctor.ensomnia.databinding.ItemContainerRecentConversionBinding


class RecentConversationsAdapter(
    var chatMessages: ArrayList<ChatMessage>,
) :
    RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder>() {
    lateinit var onConversationClick:((User)->Unit)

    inner class ConversionViewHolder(private var binding: ItemContainerRecentConversionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(chatMessage: ChatMessage) {
            Glide.with(binding.root.context).load(R.drawable.ic_baseline_person).into(binding.imageProfile)
            binding.textPatientName.text = chatMessage.conversionName
            binding.textRecentMessage.text = chatMessage.message
            binding.root.setOnClickListener {
                val user = User()
                user.id = chatMessage.conversionId!!
                user.firstName = chatMessage.conversionName!!
                onConversationClick.invoke(user)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversionViewHolder {
        return ConversionViewHolder(
            ItemContainerRecentConversionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ConversionViewHolder, position: Int) {
        holder.setData(chatMessages[position])
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rv_animation))
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }
}