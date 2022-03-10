package com.cse.ntv2.extension

import android.graphics.Paint
import android.widget.TextView

fun TextView.addStrikeThrough(){
    this.paint.run {
        flags = Paint.STRIKE_THRU_TEXT_FLAG
        isAntiAlias = true
    }

}

fun TextView.removeStrikeThrough(){
    this.paint.run {
        flags = 0
        isAntiAlias = true
    }
}