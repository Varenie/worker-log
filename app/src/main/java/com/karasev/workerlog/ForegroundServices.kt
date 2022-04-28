package com.karasev.workerlog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

open class ForegroundServices : Service() {
    var br_BatteryLevel: BroadcastReceiver? = null
    var br_turnOffReceiver: BroadcastReceiver? = null
    var br_uninstallReceiver: BroadcastReceiver? = null
    private var newBatteryLvl: Int = 0


    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotifi())
        batteryLevelReceiver()
        turnOffReceiver()
        uninstallReceiver()

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

    override fun onDestroy() {}

    private fun batteryLevelReceiver() {
        br_BatteryLevel = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val bm = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
                val batLevel: Int = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                if (batLevel != newBatteryLvl) {
                    newBatteryLvl = batLevel
                    FileManager().writeFile(
                        ": Battery level ${batLevel}%"
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
                FileManager().writeFile(
                    ": Phone turn off"
                )
            }
        }
        val filter = IntentFilter(Intent.ACTION_SHUTDOWN)
        filter.addAction(Intent.ACTION_SHUTDOWN)
        filter.addAction(Intent.EXTRA_SHUTDOWN_USERSPACE_ONLY)
        registerReceiver(br_turnOffReceiver, filter)
    }

    private fun uninstallReceiver() {
        br_uninstallReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                FileManager().writeFile(
                    ": App uninstall"
                )
            }
        }
        val filter = IntentFilter(Intent.ACTION_PACKAGE_REMOVED)
        registerReceiver(br_uninstallReceiver, filter)
    }


    companion object {
        private const val CHANNEL_ID = "while_in_use_channel_01"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 112233
    }
}