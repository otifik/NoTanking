package com.cse.ntv2.extension

import android.widget.Toast
import com.cse.ntv2.NoTankingApplication

fun shortToast(text: String){
    Toast.makeText(NoTankingApplication.context,text,Toast.LENGTH_SHORT).show()
}

fun longToast(text: String){
    Toast.makeText(NoTankingApplication.context,text,Toast.LENGTH_LONG).show()
}

fun debugToast(vararg str: String){
    val mes = StringBuilder()
    for (s in str){
        mes.append("$s ")
    }
    Toast.makeText(NoTankingApplication.context, mes.toString(), Toast.LENGTH_SHORT).show()
}