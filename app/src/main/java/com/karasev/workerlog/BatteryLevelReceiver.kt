package com.karasev.workerlog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.*
import android.os.BatteryManager
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import java.util.*

open class BatteryLevelReceiver : Service() {
    var br_BatteryLevel: BroadcastReceiver? = null
    var br_turnOffReceiver: BroadcastReceiver? = null
    var br_turnOnReceiver: BroadcastReceiver? = null
    private var newBatteryLvl: Int = 0

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotifi())
        batteryLevelReceiver()
        turnOffReceiver()
        turnOnReceiver()
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

    private fun batteryLevelReceiver() {
        br_BatteryLevel = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val bm = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
                val batLevel: Int = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                val fileManager = FileManager()
                Log.d("batterylvlv",batLevel.toString() +"\n"+newBatteryLvl)
                if (batLevel != newBatteryLvl) {
                    newBatteryLvl = batLevel
                    fileManager.writeFile(
                        ": Battery level ${batLevel}%",
                        applicationContext
                    )
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        filter.addAction(Intent.ACTION_BOOT_COMPLETED)
        registerReceiver(br_BatteryLevel, filter)

    }

    private fun turnOffReceiver() {
        br_turnOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val fileManager = FileManager()
                fileManager.writeFile(
                    ": Phone turn off",
                    applicationContext
                )
            }
        }
        val filter = IntentFilter(Intent.ACTION_SHUTDOWN)
        filter.addAction(Intent.ACTION_SHUTDOWN)
        filter.addAction(Intent.EXTRA_SHUTDOWN_USERSPACE_ONLY)
        registerReceiver(br_turnOffReceiver, filter)
    }

    private fun turnOnReceiver() {
        br_turnOnReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

            }
        }
        val filter = IntentFilter(Intent.ACTION_BOOT_COMPLETED)
        registerReceiver(br_turnOnReceiver, filter)
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
    }
}