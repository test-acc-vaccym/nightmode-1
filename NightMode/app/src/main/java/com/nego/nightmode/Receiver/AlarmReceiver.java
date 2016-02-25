package com.nego.nightmode.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Functions.NMToggle;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ALARM", intent.getAction());
        if (intent.getAction().equals(Costants.ALARM_ACTION)) {
            Log.i("ALARM", intent.getStringExtra(Costants.ALARM_EXTRA_ACTION));
            //NMToggle.startAction(context, intent.getStringExtra(Costants.ALARM_EXTRA_ACTION));
        }
    }
}