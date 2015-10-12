package com.nego.nightmode.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Utils;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.MY_PACKAGE_REPLACED")) {
            SharedPreferences SP = context.getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
            Utils.showNotification(context, SP.getBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, false));

            // TODO reimpostare allarmi per attivazione/disattivazione
        }
    }
}