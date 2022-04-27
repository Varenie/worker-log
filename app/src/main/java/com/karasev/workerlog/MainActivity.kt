package com.karasev.workerlog

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = Intent(this, BatteryLevelReceiver::class.java)
        this.startForegroundService(service)
        val sp: SharedPreferences =
            applicationContext.getSharedPreferences("LastShutDown",
                MODE_PRIVATE)
        tvBattery.text = sp.getString("LastShutDownTime", "fuck uuuu")
    }
}