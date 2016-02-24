package com.nego.nightmode.Receiver;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.nego.nightmode.Costants;
import com.nego.nightmode.R;

public class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {

    SharedPreferences SP;

    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.i("DEVADMIN", "ENABLED");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return context.getString(R.string.warning_remove_dev);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.i("DEVADMIN", "DISABLED");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
    }
}
