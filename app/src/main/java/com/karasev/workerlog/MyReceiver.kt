package com.karasev.workerlog

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast


class MyReceiver : BroadcastReceiver() {
    var context: Context? = null
    override fun onReceive(context: Context?, intent: Intent) {
        this.context = context
        Log.d(
            " BroadcastReceiverss ", "onReceive called "
                    + " PACKAGE_REMOVED "
        )
        // when package removed
        if (intent.action == "android.intent.action.PACKAGE_REMOVED") {
            Log.e(
                " BroadcastReceiverss ", "onReceive called "
                        + " PACKAGE_REMOVED "
            )
            Toast.makeText(
                context, " onReceive !!!! PACKAGE_REMOVED",
                Toast.LENGTH_LONG
            ).show()
        } else if (intent.action ==
            "android.intent.action.PACKAGE_ADDED"
        ) {
            Log.e(" BroadcastReceiverss ", "onReceive called " + "PACKAGE_ADDED")
            Toast.makeText(
                context, " onReceive !!!!." + "PACKAGE_ADDED",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}