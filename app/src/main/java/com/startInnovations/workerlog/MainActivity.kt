package com.startInnovations.workerlog

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (PermissionUtils().checkPermissionsStorage(this) &&
            PermissionUtils().checkPermissionsLocation(this)) {
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
        if (!isAgain) {
            val fileManager = FileManager()
            fileManager.writeFile(": First launch")
            val e: SharedPreferences.Editor = sp.edit()
            e.putBoolean("isAgain", true)
            e.apply()
            launchService()
        }
    }

    private fun requestPermission() {
        if (!PermissionUtils().checkPermissionsLocation(this) ||
            !PermissionUtils().checkPermissionsStorage(this)) {
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
                        grantResults.all { it == PackageManager.PERMISSION_GRANTED})) {
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