package com.karasev.workerlog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;
public class Restart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("LastShutDown", context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("LastShutDownTime", Calendar.getInstance().getTime().toString());
        et.commit();
    }
}
