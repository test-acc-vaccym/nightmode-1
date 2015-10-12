package com.nego.nightmode.Functions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Receiver.AlarmReceiver;

import java.util.Calendar;

public class AlarmF {


    public static void addAlarm(Context context, int id, long time, String repeat) {

        AlarmManager alarmMgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmReceiver.class);
        intent.setAction(Costants.ALARM_ACTION);
        PendingIntent alarmIntent= PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY * 7, alarmIntent);
    }

    public static void deleteAlarm(Context context, int id) {
        AlarmManager manager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context.getApplicationContext(), AlarmReceiver.class);
        intent.setAction(Costants.ALARM_ACTION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(alarmIntent);
        alarmIntent.cancel();
    }

    public static void updateAlarm(Context context, int id, long time, String repeat) {
        deleteAlarm(context, id);
        addAlarm(context, id, time, repeat);
    }
}
