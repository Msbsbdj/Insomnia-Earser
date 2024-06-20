package com.doctor.ensomnia.data.pojo

import java.io.Serializable

class Doctor : Serializable {
    var id: String? = null
    var name: String? = null
    var email:String? = null
    var password:String? = null
    var image: String? = null
    var phoneNumber: String? = null
    var fcmToken: String? = null
}