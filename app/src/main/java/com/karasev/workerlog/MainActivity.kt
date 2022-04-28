package com.karasev.workerlog

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var st: String?
        val service = Intent(this, BatteryLevelReceiver::class.java)
        this.startForegroundService(service)
        firstLaunch()
        val fileManager = FileManager()
        st = fileManager.readFile(applicationContext)
        tvBattery.text = st
    }

    private fun firstLaunch(){
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val isAgain = sp.getBoolean("isAgain", false)

        if (isAgain) {
            Log.d("batterylvlv","Not first Launch")
        } else{
            val fileManager = FileManager()
            fileManager.writeFile(": First launch",applicationContext)
            val e: SharedPreferences.Editor = sp.edit()
            e.putBoolean("isAgain", true)
            e.apply()
        }
    }
}