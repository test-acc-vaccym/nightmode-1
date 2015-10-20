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


    public static int addAlarm(Context context, int id, long time, String todo) {

        AlarmManager alarmMgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmReceiver.class);
        intent.setAction(Costants.ALARM_ACTION);
        intent.putExtra(Costants.ALARM_EXTRA_ACTION, todo);
        PendingIntent alarmIntent= PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= 19)
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY * 7, alarmIntent);
        else if (Build.VERSION.SDK_INT >= 15)
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time, 0, alarmIntent);

        // TODO resolve pending id
        Log.i("created alarm", id + " " + time);
        Log.i("created alarm when", Utils.getDate(context, time));
        return alarmIntent.getCreatorUid();
    }

    public static void deleteAlarm(Context context, int id) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmReceiver.class);
        intent.setAction(Costants.ALARM_ACTION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(alarmIntent);
        alarmIntent.cancel();
        Log.i("deleted alarm", "" + id);
    }
}
