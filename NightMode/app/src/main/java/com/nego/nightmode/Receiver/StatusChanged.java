package com.nego.nightmode.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Functions.NMToggle;
import com.nego.nightmode.Utils;

public class StatusChanged extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Costants.ACTION_NIGHT_MODE_OFF)) {
            NMToggle.startAction(context, Costants.ACTION_NIGHT_MODE_OFF);
        }
        
        if (intent.getAction().equals(Costants.ACTION_NIGHT_MODE_ON)) {
            NMToggle.startAction(context, Costants.ACTION_NIGHT_MODE_ON);
        }
    }
}
