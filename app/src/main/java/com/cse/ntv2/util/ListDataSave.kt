package com.cse.ntv2.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonParser

class ListDataSave(context: Context,preferenceName: String) {

    private val preference: SharedPreferences = context.getSharedPreferences(preferenceName,Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preference.edit()

    fun <T> saveDataList(tag: String, dataList: List<T>){
        val gson = Gson()
        val str = gson.toJson(dataList)
        editor.clear()
        editor.putString(tag,str)
        editor.commit()
    }

    fun <T> getDataList(tag: String,clazz: Class<T>): List<T>{
        val list: MutableList<T> = ArrayList()
        val str = preference.getString(tag, null) ?: return list
        val gson = Gson()
        val jsonArray = JsonParser().parse(str).asJsonArray
        for (json in jsonArray){
            list.add(gson.fromJson(json,clazz))
        }
        return list
    }

}