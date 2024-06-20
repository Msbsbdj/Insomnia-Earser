package com.marco.ensominaearser.ui.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marco.ensominaearser.R
import com.marco.ensominaearser.data.pojo.ChatMessage
import com.marco.ensominaearser.data.pojo.Doctor
import com.marco.ensominaearser.data.pojo.Meal
import com.marco.ensominaearser.databinding.ItemContainerRecentConversionBinding

class RecentConversationsAdapter(
    var chatMessages: ArrayList<ChatMessage>,
) :
    RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder>() {
    lateinit var onDoctorClick:((Doctor)->Unit)

    inner class ConversionViewHolder(private var binding: ItemContainerRecentConversionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(chatMessage: ChatMessage) {
            Glide.with(binding.root.context).load(chatMessage.conversionImage).into(binding.imageProfile)
            binding.textDoctorName.text = chatMessage.conversionName
            binding.textRecentMessage.text = chatMessage.message
            binding.root.setOnClickListener {
                val doctor = Doctor()
                doctor.id = chatMessage.conversionId
                doctor.name = chatMessage.conversionName
                doctor.image = chatMessage.conversionImage
                onDoctorClick.invoke(doctor)
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