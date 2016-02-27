package com.nego.nightmode.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Functions.NMToggle;
import com.nego.nightmode.Mode;
import com.nego.nightmode.Utils;
import com.nego.nightmode.database.DbAdapter;

import java.util.Calendar;

public class StatusChanged extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Costants.ACTION_NIGHT_MODE_OFF)) {
            NMToggle.startAction(context, Utils.getDayMode(context));
        }
    }
}
