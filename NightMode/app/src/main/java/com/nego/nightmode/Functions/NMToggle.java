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

            Log.i("NEGO_M", m.getName());

            SharedPreferences SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
            SP.edit().putInt(Costants.ACTUAL_MODE, m.getId()).apply();

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
            WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            if (!m.getName().equals(Costants.DEFAULT_MODE_DAY)) {
                if (m.getWifi()) {
                    if (day != null)
                        day.setWifi(wm.isWifiEnabled());
                    wm.setWifiEnabled(false);
                }
            } else {
                wm.setWifiEnabled(m.getWifi());
            }

            // BLUETOOTH
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!m.getName().equals(Costants.DEFAULT_MODE_DAY)) {
                if (m.getBluetooth()) {
                    if (day != null)
                        day.setBluetooth(mBluetoothAdapter.isEnabled());
                    mBluetoothAdapter.disable();
                }
            } else {
                if (m.getBluetooth())
                    mBluetoothAdapter.enable();
                else
                    mBluetoothAdapter.disable();
            }

            Log.i("NEGO_M", "BLUETOOTH OK");

            // ALARM VOLUME
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (!m.getName().equals(Costants.DEFAULT_MODE_DAY)) {
                if (m.getAlarm_sound()) {
                    if (day != null) {
                        day.setAlarm_level(am.getStreamVolume(AudioManager.STREAM_ALARM));
                    }
                    am.setStreamVolume(AudioManager.STREAM_ALARM, m.getAlarm_level(), 0);
                }
            } else {
                am.setStreamVolume(AudioManager.STREAM_ALARM, m.getAlarm_level(), 0);
            }

            Log.i("NEGO_M", "ALARM VOLUME OK");

            // DO NOT DISTURB
            if (!m.getName().equals(Costants.DEFAULT_MODE_DAY)) {
                if (m.getDo_no_disturb()) {
                    if (day != null)
                        day.setDo_no_disturb(AudioManager.RINGER_MODE_NORMAL);
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                }
            } else {
                am.setRingerMode(m.getDo_no_disturbDB());
            }

            Log.i("NEGO_M", "DO NOT DISTURB " + m.getDo_no_disturbDB());

            //UPDATE UI
            Utils.showNotification(this, m);
            Utils.updateWidget(this);

            if (!m.getName().equals(Costants.DEFAULT_MODE_DAY)) {
                ModeService.startAction(this, Costants.ACTION_UPDATE, day);
            } else {
                sendResponse(Costants.ACTION_NIGHT_MODE_TOGGLE);
            }

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
