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
import android.widget.Toast

open class BatteryLevelReceiver : Service() {
    var br_ScreenOffReceiver: BroadcastReceiver? = null

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID,createNotification())
        batteryLevelReciver()
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    private fun createNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .build()

    override fun onDestroy() {

    }

    private fun batteryLevelReciver() {
        br_ScreenOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val batteryPct = level * 100 / scale.toFloat()
                Toast.makeText(applicationContext, "Scale: ${scale} \n Level${scale} \n${batteryPct}%", Toast.LENGTH_SHORT).show()
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