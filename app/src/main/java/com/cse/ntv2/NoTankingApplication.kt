package com.cse.ntv2

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.kongzue.dialogx.DialogX

class NoTankingApplication: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        DialogX.init(context)
    }
}