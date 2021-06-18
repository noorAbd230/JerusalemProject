package com.example.jerusalemapplication.model

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private var mySharedPreferences: SharedPreferences = context.getSharedPreferences("fileName",Context.MODE_PRIVATE)

    fun setNightModeState(state:Boolean?){
        val editor = mySharedPreferences.edit()
        editor.putBoolean("Night Mode",state!!)
        editor.commit()

    }
    fun loadNightModeState():Boolean{
        return mySharedPreferences.getBoolean("Night Mode",false)
    }
}
