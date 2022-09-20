package com.startInnovations.workerlog

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.Boolean as Boolean

class PermissionUtils {
    private var isAlreadyBeThisDialog = false

    fun checkPermissionsStorage(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else
            (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
            (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
    }

    fun checkPermissionsLocation(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun checkPermissionsReapPhone(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun requestPermissions(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE),
            requestCode
        )
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isAlreadyBeThisDialog) {
//            checkBackgroundLocationPermissionAPI30(requestCode, activity)
//            isAlreadyBeThisDialog = true
//            checkExternalStoragePermissionAPI30(requestCode, activity)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkBackgroundLocationPermissionAPI30(requestCode: Int, activity: Activity) {
        if (checkBackgroundLocationPermission(activity)) return
        AlertDialog.Builder(activity)
            .setTitle("Доступ к геолокации")
            .setMessage("Перети в настройки для разрешения доступа к геолокации в фоне?")
            .setPositiveButton("Да") { _,_ ->
                // this request will take user to Application's Setting page
                requestPermissionBackground(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), requestCode, activity)
            }
            .setNegativeButton("Нет") { dialog,_ ->
                dialog.dismiss()
            }
            .create()
            .show()

    }

    private fun requestPermissionBackground(arrayOf: Array<String>, requestCode: Int, activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf, requestCode)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun checkBackgroundLocationPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

}