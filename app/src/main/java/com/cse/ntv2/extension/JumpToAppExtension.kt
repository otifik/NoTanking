package com.cse.ntv2.extension

import android.content.ComponentName
import android.content.Intent
import com.cse.ntv2.NoTankingApplication

const val WEIBO = "weibo"
const val WBPKG = "com.sina.weibo"
const val WBCLS = "com.sina.weibo.SplashActivity"
const val QQ = "qq"
const val QQPKG = "com.tencent.mobileqq"
const val QQCLS = "com.tencent.mobileqq.activity.SplashActivity"
const val WEIXIN = "weixin"
const val WXPKG = "com.tencent.mm"
const val WXCLS = "com.tencent.mm.ui.LauncherUI"
const val MIHOYO = "mihoyo"
const val MHYPKG = "com.mihoyo.hyperion"
const val MHYCLS = "com.mihoyo.hyperion.ui.SplashActivity"
const val EMPTYAPP = "NULL"
val APP_AVAILABLE = arrayOf(EMPTYAPP, WEIBO, QQ, WEIXIN, MIHOYO)
val APP_NAME = arrayOf("无","微博","腾讯QQ","微信","米游社")
//val APP_MAP = mapOf(
//    Pair(APP_NAME[0], APP_AVAILABLE[0]),
//    Pair(APP_NAME[1], APP_AVAILABLE[1]),
//    Pair(APP_NAME[2], APP_AVAILABLE[2]),
//    Pair(APP_NAME[3], APP_AVAILABLE[3]),
//    Pair(APP_NAME[4], APP_AVAILABLE[4]))

fun jumpToAPP(appStr: String){
    val intent = Intent()
    var component: ComponentName? = null
    when(appStr){
        WEIBO -> component = ComponentName(WBPKG, WBCLS)
        QQ -> component = ComponentName(QQPKG, QQCLS)
        WEIXIN -> component = ComponentName(WXPKG, WXCLS)
        MIHOYO -> component = ComponentName(MHYPKG, MHYCLS)
    }
    intent.component = component
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    NoTankingApplication.context.startActivity(intent)
}