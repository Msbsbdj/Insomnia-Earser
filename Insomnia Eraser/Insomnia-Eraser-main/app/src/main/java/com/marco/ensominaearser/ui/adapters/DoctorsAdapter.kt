package com.marco.ensominaearser.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marco.ensominaearser.data.pojo.Book
import com.marco.ensominaearser.data.pojo.Doctor
import com.marco.ensominaearser.databinding.ItemContainerDoctorBinding

class DoctorsAdapter :
    RecyclerView.Adapter<DoctorsAdapter.DoctorsViewHolder> {
    private var mDoctors: ArrayList<Doctor> = ArrayList()
    lateinit var onDoctorClick:((Doctor)->Unit)

    constructor()
    constructor(doctors: ArrayList<Doctor>) {
        this.mDoctors = doctors

    }

    inner class DoctorsViewHolder(private var itemContainerDoctorBinding: ItemContainerDoctorBinding) :
        RecyclerView.ViewHolder(itemContainerDoctorBinding.root) {
        @SuppressLint("SetTextI18n")
        fun setLabData(doctor: Doctor) {
            itemContainerDoctorBinding.patientName.text =
                "${doctor.name} "
            Glide.with(itemContainerDoctorBinding.root.context).load(doctor.image).into(itemContainerDoctorBinding.doctorImageProfile)
            itemContainerDoctorBinding.root.setOnClickListener {
   onDoctorClick.invoke(doctor)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsViewHolder {
        val binding =
            ItemContainerDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorsViewHolder, position: Int) {
        holder.setLabData(mDoctors[position])
    }

    override fun getItemCount(): Int {
        return mDoctors.size
    }
}