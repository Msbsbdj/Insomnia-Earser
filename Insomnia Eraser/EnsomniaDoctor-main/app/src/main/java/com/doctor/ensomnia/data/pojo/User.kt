package com.doctor.ensomnia.data.pojo

import java.io.Serializable

data class User(
    var id:String = "" ,
    var firstName:String = "",
    var lastName:String = "",
    var email:String = "",
    var password:String = "",
    var phone:String = " ",
    var gender:String = " "
):Serializable
