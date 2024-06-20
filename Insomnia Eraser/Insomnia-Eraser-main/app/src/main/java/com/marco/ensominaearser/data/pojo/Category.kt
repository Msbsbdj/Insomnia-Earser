package com.marco.ensominaearser.data.pojo

import java.io.Serializable

data class Category(
    var name:String = "" ,
    var coverurl:String = "",
    var songs : ArrayList<String>  = ArrayList()

): Serializable
