package com.karasev.workerlog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileWriter
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

open class BatteryLevelReceiver : Service() {
    var br_ScreenOffReceiver: BroadcastReceiver? = null
    private var newBatteryLvl: Int = 0

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID,createNotifi())
        batteryLevelReciver()
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }
    private fun createNotifi() = NotificationCompat.Builder(this, CHANNEL_ID).build()

    override fun onDestroy() {

    }

    private fun batteryLevelReciver() {
        br_ScreenOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val bm = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
                val batLevel:Int = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                if(batLevel != newBatteryLvl){
                    newBatteryLvl = batLevel
                    Toast.makeText(applicationContext, "${Calendar.getInstance().time
                    }\n${batLevel}%", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(br_ScreenOffReceiver, filter)

    }

    companion object {

        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
    }
}