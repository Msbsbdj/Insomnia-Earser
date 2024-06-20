package com.marco.ensominaearser.data.pojo

import java.io.Serializable

data class Book(
    var name:String ="",
    var  bookDesc:String="",
    var writerName:String="",
    var bookPhoto:String="",
    var bookPdf:String="",
    var bookRate:Float = 0.0f
    ) :Serializable
