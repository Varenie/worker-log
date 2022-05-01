package com.karasev.workerlog

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.*
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

open class ForegroundServices : Service() {
    var br_BatteryLevel: BroadcastReceiver? = null
    var br_turnOffReceiver: BroadcastReceiver? = null

    private var newBatteryLvl: Int = 0

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }



    override fun onCreate() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotifi())
        batteryLevelReceiver()
        turnOffReceiver()
        LocationListener()
    }

    private fun LocationListener() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(2)
            fastestInterval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(15)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.equals(null)) {
                    return
                }
                for (location in locationResult.locations) {
                    val Lat = java.lang.String.valueOf(location.latitude)
                    val Lon = java.lang.String.valueOf(location.longitude)
                    FileManager().writeFile(": Location $Lat - $Lon")
                }
            }

        }
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        fusedLocationClient!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback!!,
            Looper.getMainLooper()
        )
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

    companion object {
        private const val CHANNEL_ID = "while_in_use_channel_01"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 112233
    }
}