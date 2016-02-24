package com.nego.nightmode.Functions;

import android.app.IntentService;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.util.Log;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Mode;
import com.nego.nightmode.R;
import com.nego.nightmode.Receiver.DeviceAdminReceiver;
import com.nego.nightmode.Utils;
import com.nego.nightmode.database.DbAdapter;

import java.util.Calendar;

public class NMToggle extends IntentService {

    public static void startAction(Context context, Mode m) {
        Intent intent = new Intent(context, NMToggle.class);
        intent.setAction(Costants.ACTION_NIGHT_MODE_TOGGLE);
        intent.putExtra(Costants.MODE_EXTRA, m);
        context.startService(intent);
    }

    private void sendResponse(String s) {
        Intent i = new Intent(s);
        sendBroadcast(i);
    }

    public NMToggle() {
        super("NMToggle");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction().equals(Costants.ACTION_NIGHT_MODE_TOGGLE)) {
            Mode m = intent.getParcelableExtra(Costants.MODE_EXTRA);

            SharedPreferences SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
            boolean rememberStatus = SP.getBoolean(Costants.PREFERENCES_REMEMBER_OLD, true);

            // UPDATE MODE LAST ACTIVATION
            DbAdapter dbHelper = new DbAdapter(this);
            dbHelper.open();
            m.setLast_activation(Calendar.getInstance().getTimeInMillis());
            m.update(dbHelper);

            // GET DAY MODE
            Cursor c = dbHelper.getModeByName(Costants.DEFAULT_MODE_DAY);
            Mode day = null;
            if (c.moveToFirst())
                day = new Mode(c);
            c.close();

            dbHelper.close();

            //WIFI
            if (m.getWifi()) {
                WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                if (!m.getName().equals(Costants.DEFAULT_MODE_DAY)) {
                    if (rememberStatus && day != null)
                        day.setWifi(wm.isWifiEnabled());
                    wm.setWifiEnabled(false);
                } else {
                    wm.setWifiEnabled(m.getWifi());
                }
            }

            // BLUETOOTH
            if (m.getBluetooth()) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (rememberStatus && day != null)
                    day.setBluetooth(mBluetoothAdapter.isEnabled());
                mBluetoothAdapter.disable();
            }

            // ALARM VOLUME
            if (m.getAlarm_sound()) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (rememberStatus && day != null) {
                    day.setAlarm_level(am.getStreamVolume(AudioManager.STREAM_ALARM));
                }
                am.setStreamVolume(AudioManager.STREAM_ALARM, m.getAlarm_level(), 0);
            }

            // DO NOT DISTURB
            if (m.getDo_no_disturb()) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (rememberStatus && day != null)
                    day.setDo_no_disturb(am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL);
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }

            //UPDATE UI
            Utils.showNotification(this, m);
            Utils.updateWidget(this);
            sendResponse(Costants.ACTION_NIGHT_MODE_TOGGLE);

            // SCREEN OFF
            if (m.getScreen_off()) {
                final DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName adminReceiver = new ComponentName(NMToggle.this, DeviceAdminReceiver.class);
                if (policyManager.isAdminActive(adminReceiver)) {
                    Log.i("SCREEN OFF", "Going to sleep now.");
                    policyManager.lockNow();
                } else {
                    Log.i("SCREEN OFF", "Not an admin");
                }
            }
        }
    }

}
