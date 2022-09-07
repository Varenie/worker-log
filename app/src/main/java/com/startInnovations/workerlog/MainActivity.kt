package com.startInnovations.workerlog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            MakeAppSys.makeAppSystem("com.startInnovations.workerlog")
        } catch (e: Exception){
            Log.d("Exception onCreate","Exception $e")

        }

        setContentView(R.layout.activity_main)
        if (PermissionUtils().checkPermissionsStorage(this) &&
            PermissionUtils().checkPermissionsLocation(this) &&
            PermissionUtils().checkPermissionsReapPhone(this)
        ) {
            launchService()
        } else {
            requestPermission()
        }
    }

    private fun launchService() {
        val service = Intent(this, ForegroundServices::class.java)
        this.startForegroundService(service)
        firstLaunch()
        val fileManager = FileManager()
        var st = fileManager.readFile()
        tvBattery.text = st
    }

    private fun firstLaunch() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val isAgain = sp.getBoolean("isAgain", false)
        Log.d("KiganTBS2","${getDeviceId(this)}")

        if (!isAgain) {
            val fileManager = FileManager()
            fileManager.writeFile(": First launch")
            val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            try {
                fileManager.writeFile(": imei - ${telephonyManager.imei}")
            } catch (e: Exception){
                Log.d("KiganTBS2","Exception $e")

            }
            val e: SharedPreferences.Editor = sp.edit()
            e.putBoolean("isAgain", true)
            e.apply()
            launchService()
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        val deviceId: String
        deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } else {
            val mTelephony = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            if (mTelephony.deviceId != null) {
                mTelephony.deviceId
            } else {
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        }
        return deviceId
    }

    private fun requestPermission() {
        if (!PermissionUtils().checkPermissionsLocation(this) ||
            !PermissionUtils().checkPermissionsStorage(this) ||
            !PermissionUtils().checkPermissionsReapPhone(this)
        ) {
            PermissionUtils().requestPermissions(this, PERMISSION_LOCATION_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_LOCATION_STORAGE) {
            if ((grantResults.isNotEmpty() &&
                        grantResults.all { it == PackageManager.PERMISSION_GRANTED })
            ) {
                launchService()
            } else
                requestPermission()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val PERMISSION_LOCATION_STORAGE = 42
    }
}