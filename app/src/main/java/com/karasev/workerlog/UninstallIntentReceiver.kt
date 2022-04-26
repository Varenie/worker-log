package com.karasev.workerlog

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class UninstallIntentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        // fetching package names from extras
        val packageNames = intent.getStringArrayExtra("android.intent.extra.PACKAGES")
        if (packageNames != null) {
            for (packageName in packageNames) {
                if (packageName != null && packageName == "YOUR_APPLICATION_PACKAGE_NAME") {
                    // User has selected our application under the Manage Apps settings
                    // now initiating background thread to watch for activity
                    ListenActivities(context).start()

                }
                Log.d(
                    "topActivityss", "CURRENT Activity ::"
                )
            }
        }
    }
}