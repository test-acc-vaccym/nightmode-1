package com.nego.nightmode.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nego.nightmode.Costants;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Costants.ALARM_ACTION)) {
            try {
                // TODO: attivazione/disattivazione night mode in base agli orari
            } catch (Exception e) {}
        }
    }
}