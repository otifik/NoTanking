package com.cse.ntv2.ui.settings

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.fragment.app.activityViewModels
import com.cse.ntv2.ui.main.MainActivity
import com.cse.ntv2.ui.main.MainViewModel
import com.cse.ntv2.R
import com.cse.ntv2.base.BaseFragment
import com.cse.ntv2.databinding.FragmentSettingsBinding
import com.cse.ntv2.extension.debugToast
import com.github.iielse.switchbutton.SwitchView

const val PERMANENT_NOTIFICATION_ID = 0
const val PERMANENT_CHANNEL_ID = "permanent"
const val PERMANENT_CHANNEL_NAME = "PERMANENT"
const val PERMANENT_NOTIFICATION_SWITCH_STATUS = "PNSwitch"
const val PNS_ON = "ON"
const val PNS_OFF = "OFF"
const val AUTO_DONE = "AUTO_DONE"
const val SETTINGS_NAME = "SETTINGS"

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private lateinit var switchPermanentNotice: SwitchView
    private lateinit var switchAutoDone: SwitchView
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: MainViewModel by activityViewModels()

    private var num: Int = 0

    override fun initRef() {
        switchPermanentNotice = binding.switchPermanentNotice
        switchAutoDone = binding.switchAutoDone
        sharedPreferences = initSharedPreferences()
    }

    override fun initView() {
        viewModel.count.observe(this){
            num = it
        }
        initSwitch()
    }

    private fun initSwitch(){
        val manager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(PERMANENT_CHANNEL_ID, PERMANENT_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)

        if (sharedPreferences.getString(PERMANENT_NOTIFICATION_SWITCH_STATUS, PNS_OFF) == PNS_ON){
            switchPermanentNotice.toggleSwitch(true)
        }
        if (sharedPreferences.getBoolean(AUTO_DONE,false)){
            switchAutoDone.toggleSwitch(true)
        }
        viewModel.todoLiveData.observe(this){
            viewModel.todoLiveData.postValue(it)
            val status = sharedPreferences.getString(PERMANENT_NOTIFICATION_SWITCH_STATUS, PNS_OFF)
            if (status == PNS_ON){
                manager.notify(PERMANENT_NOTIFICATION_ID,createPermanentNotification())
            }
        }

        switchPermanentNotice.setOnStateChangedListener(object : SwitchView.OnStateChangedListener{
            override fun toggleToOn(view: SwitchView) {
                manager.notify(PERMANENT_NOTIFICATION_ID,createPermanentNotification())
                val edit = sharedPreferences.edit()
                edit.putString(PERMANENT_NOTIFICATION_SWITCH_STATUS, PNS_ON)
                edit.apply()
                view.toggleSwitch(true)
            }

            override fun toggleToOff(view: SwitchView) {
                manager.cancel(PERMANENT_NOTIFICATION_ID)
                val edit = sharedPreferences.edit()
                edit.putString(PERMANENT_NOTIFICATION_SWITCH_STATUS, PNS_OFF)
                edit.apply()
                view.toggleSwitch(false)
            }
        })

        switchAutoDone.setOnStateChangedListener(object : SwitchView.OnStateChangedListener{
            override fun toggleToOn(view: SwitchView) {
                val edit = sharedPreferences.edit()
                edit.putBoolean(AUTO_DONE, true)
                edit.apply()
                view.toggleSwitch(true)
            }

            override fun toggleToOff(view: SwitchView) {
                val edit = sharedPreferences.edit()
                edit.putBoolean(AUTO_DONE, false)
                edit.apply()
                view.toggleSwitch(false)
            }
        })


    }

    fun createPermanentNotification(): Notification {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        val pi = PendingIntent.getActivity(requireActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(requireActivity(), PERMANENT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.icon)
            .setContentTitle("NoTanking")
            .setContentText(when(num){
                0 -> "恭喜你，已完成所有TODO"
                else -> "目前剩余${num}条TODO"
            })
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pi)
            .setAutoCancel(false)
            .setOngoing(true)
            .setShowWhen(false)
            .build()
    }

    private fun initSharedPreferences(): SharedPreferences = requireActivity().getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE)

    override fun initLiveData() {}

}