package com.doctor.ensomnia.utilites

import com.doctor.ensomnia.data.pojo.User


interface ConversionListener {
    fun onConversionClicked(patient: User)
}