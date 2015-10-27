package com.nego.nightmode.Functions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Receiver.AlarmReceiver;
import com.nego.nightmode.Utils;

import java.util.Calendar;

public class AlarmF {


    public static void addAlarm(Context context, int id, long time, String todo) {

        AlarmManager alarmMgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmReceiver.class);
        intent.setAction(Costants.ALARM_ACTION);
        intent.putExtra(Costants.ALARM_EXTRA_ACTION, todo);
        PendingIntent alarmIntent= PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= 19)
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY * 7, alarmIntent);
        else if (Build.VERSION.SDK_INT >= 15)
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY * 7, alarmIntent);
        Log.i("ID CREATE", id + " - " + Utils.getDate(context, time));
    }

    public static void deleteAlarm(Context context, int id) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmReceiver.class);
        intent.setAction(Costants.ALARM_ACTION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_NO_CREATE);
        manager.cancel(alarmIntent);
        Log.i("ID DELETE", "" + id);
    }
}
