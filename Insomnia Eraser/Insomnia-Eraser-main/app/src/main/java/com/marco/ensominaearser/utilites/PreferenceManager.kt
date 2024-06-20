package com.marco.ensominaearser.utilites

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context:Context) {
   private var sharedPreferences:SharedPreferences? = null
    
    init {
        sharedPreferences = context.getSharedPreferences(Constants.databasename,Context.MODE_PRIVATE)
    }
    fun putString(key:String,value: String ){
        val editor = sharedPreferences?.edit()
        editor!!.putString(key, value)
        editor.apply()
    }

    fun getString(key: String):String{
        return sharedPreferences!!.getString(key,null)!!
    }
    fun putInt(key:String,value: Int ){
        val editor = sharedPreferences?.edit()
        editor!!.putInt(key, value)
        editor.apply()
    }
    fun getInt(key: String):Int{
        return sharedPreferences!!.getInt(key,0)
    }

    fun putBoolean(key: String, value: Boolean){
    val editor = sharedPreferences?.edit()
    editor!!.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String):Boolean{
        return sharedPreferences!!.getBoolean(key,false)
    }

    fun clear(){
        val editor = sharedPreferences?.edit()
        editor!!.clear()
        editor.apply()
    }

}