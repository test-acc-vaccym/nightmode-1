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
import android.widget.Toast;

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
            SP.edit().putInt(Costants.ACTUAL_MODE, m.getId()).commit();

            // UPDATE MODE LAST ACTIVATION
            m.setLast_activation(Calendar.getInstance().getTimeInMillis());
            ModeService.startAction(this, Costants.ACTION_UPDATE, m, false);

            // GET DAY MODE
            Mode day = Utils.getDayMode(this);
            if (day == null) {
                Toast.makeText(this, getString(R.string.text_error), Toast.LENGTH_SHORT).show();
                return;
            }

            //WIFI
            WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            if (!m.isDayMode()) {
                day.setWifi(wm.isWifiEnabled());
                if (m.getWifi()) {
                    wm.setWifiEnabled(false);
                }
            } else {
                wm.setWifiEnabled(m.getWifi());
            }

            // BLUETOOTH
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!m.isDayMode()) {
                day.setBluetooth(mBluetoothAdapter.isEnabled());
                if (m.getBluetooth()) {
                    mBluetoothAdapter.disable();
                }
            } else {
                if (m.getBluetooth())
                    mBluetoothAdapter.enable();
                else
                    mBluetoothAdapter.disable();
            }

            // ALARM VOLUME
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (!m.isDayMode()) {
                day.setAlarm_level(am.getStreamVolume(AudioManager.STREAM_ALARM));
                if (m.getAlarm_sound()) {
                    day.setAlarm_sound(true);
                    am.setStreamVolume(AudioManager.STREAM_ALARM, m.getAlarm_level(), 0);
                } else {
                    day.setAlarm_sound(false);
                }
            } else {
                if (day.getAlarm_sound())
                    am.setStreamVolume(AudioManager.STREAM_ALARM, m.getAlarm_level(), 0);
            }

            // DO NOT DISTURB
            if (!m.isDayMode()) {
                SP.edit().putInt(Costants.PREFERENCES_DO_NOT_DISTURB_OLD, am.getRingerMode()).apply();
                if (m.getDo_no_disturb()) {
                    day.setDo_no_disturb(true);
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                } else {
                    day.setDo_no_disturb(false);
                }
            } else {
                if (m.getDo_no_disturb())
                    am.setRingerMode(SP.getInt(Costants.PREFERENCES_DO_NOT_DISTURB_OLD, AudioManager.RINGER_MODE_NORMAL));
            }

            //UPDATE UI
            Utils.showNotification(this, m);
            Utils.updateWidget(this); // TODO Widget

            ModeService.startAction(this, Costants.ACTION_UPDATE, day, false);
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
