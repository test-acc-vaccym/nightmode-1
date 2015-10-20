package com.nego.nightmode.Functions;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nego.nightmode.Alarm;
import com.nego.nightmode.Costants;
import com.nego.nightmode.database.DbAdapter;

public class AlarmService extends IntentService {

    public static void startAction(Context context, String action, Alarm a) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.setAction(action);
        intent.putExtra(Costants.ALARM_EXTRA, a);
        context.startService(intent);
    }

    private void sendResponse(String s) {
        Intent i = new Intent(s);
        sendBroadcast(i);
    }

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Costants.ACTION_CREATE.equals(action)) {
                final Alarm a = intent.getParcelableExtra(Costants.ALARM_EXTRA);
                createAlarm(a);
            }else if (Costants.ACTION_DELETE.equals(action)) {
                final Alarm a = intent.getParcelableExtra(Costants.ALARM_EXTRA);
                deleteAlarm(a);
            }
        }
    }

    private void createAlarm(Alarm a) {
        DbAdapter dbHelper = new DbAdapter(this);
        dbHelper.open();
        if (a.create_alarm(this, dbHelper)) {
            sendResponse(Costants.ACTION_CREATE);
        }
        dbHelper.close();
    }

    private void deleteAlarm(Alarm a) {
        DbAdapter dbHelper = new DbAdapter(this);
        dbHelper.open();
        if (a.delete_alarm(this, dbHelper)) {
            sendResponse(Costants.ACTION_DELETE);
        }
        dbHelper.close();
    }


}
