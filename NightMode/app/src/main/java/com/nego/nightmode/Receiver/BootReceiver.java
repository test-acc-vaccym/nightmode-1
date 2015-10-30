package com.nego.nightmode.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.nego.nightmode.Alarm;
import com.nego.nightmode.Costants;
import com.nego.nightmode.Functions.AlarmF;
import com.nego.nightmode.Functions.AlarmService;
import com.nego.nightmode.Utils;
import com.nego.nightmode.database.DbAdapter;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.MY_PACKAGE_REPLACED")) {
            SharedPreferences SP = context.getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
            Utils.showNotification(context, SP.getBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, false));


            DbAdapter dbHelper = new DbAdapter(context);
            dbHelper.open();
            Cursor c = dbHelper.fetchAllAlarms();
            while (c.moveToNext()) {
                Alarm a = new Alarm(c);
                AlarmF.addAlarm(context, a.getId() + 10000, a.getStart(), Costants.ACTION_NIGHT_MODE_ON);
                AlarmF.addAlarm(context, a.getId(), a.getEnd(), Costants.ACTION_NIGHT_MODE_OFF);
            }
            dbHelper.close();
        }
    }
}