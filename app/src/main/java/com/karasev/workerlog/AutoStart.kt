package com.karasev.workerlog

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AutoStart: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val service = Intent(context, BatteryLevelReceiver::class.java)
        context?.startForegroundService(service)

        val intent1 = Intent(context, MainActivity::class.java)
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context!!.startActivity(intent1)
    }
}