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
    var br_uninstallReceiver: BroadcastReceiver? = null
    private var newBatteryLvl: Int = 0

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID,createNotifi())
        batteryLevelReceiver()
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

    override fun onDestroy() {

    }

    private fun batteryLevelReceiver() {
        br_BatteryLevel = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d("batterylvlv","NE NADO DAD fake")

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
        registerReceiver(br_BatteryLevel, filter)

    }

    private fun uninstallReceiver(){
        br_uninstallReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val sp: SharedPreferences = applicationContext.getSharedPreferences("LastShutDown", MODE_PRIVATE)
                val et = sp.edit()
                et.putString("LastShutDownTime", Calendar.getInstance().time.toString()+"zahuyaril konkretno app")
                et.commit()
            }
        }
        val filter = IntentFilter(Intent.ACTION_REBOOT)
        filter.addAction(Intent.ACTION_SHUTDOWN)
        filter.addAction(Intent.EXTRA_SHUTDOWN_USERSPACE_ONLY)
        registerReceiver(br_uninstallReceiver,filter)
    }

    companion object {

        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
    }
}