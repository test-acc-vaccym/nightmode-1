package com.nego.nightmode.Functions;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.util.Log;

import com.nego.nightmode.Costants;
import com.nego.nightmode.R;
import com.nego.nightmode.Utils;

import java.util.Calendar;

public class NMToggle extends IntentService {

    public static void startAction(Context context, String action) {
        Intent intent = new Intent(context, NMToggle.class);
        intent.setAction(action);
        context.startService(intent);
    }

    public NMToggle() {
        super("NMToggle");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            boolean on = Costants.ACTION_NIGHT_MODE_ON.equals(action);
            SharedPreferences SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);

            //WIFI
            if (SP.getBoolean(Costants.PREFERENCES_WIFI, true)) {
                WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                if (SP.getBoolean(Costants.PREFERENCES_REMEMBER_OLD, true)) {
                    if (on) {
                        SP.edit().putBoolean(Costants.PREFERENCES_WIFI_OLD, wm.isWifiEnabled()).apply();
                        wm.setWifiEnabled(false);
                    } else {
                        wm.setWifiEnabled(SP.getBoolean(Costants.PREFERENCES_WIFI_OLD, true));
                    }
                } else {
                    wm.setWifiEnabled(!on);
                }
            }

            // BLUETOOTH
            if (SP.getBoolean(Costants.PREFERENCES_BLUETOOTH, true)) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (SP.getBoolean(Costants.PREFERENCES_REMEMBER_OLD, true)) {
                    if (on) {
                        SP.edit().putBoolean(Costants.PREFERENCES_BLUETOOTH_OLD, mBluetoothAdapter.isEnabled()).apply();
                        mBluetoothAdapter.disable();
                    } else {
                        if (SP.getBoolean(Costants.PREFERENCES_BLUETOOTH_OLD, true))
                            mBluetoothAdapter.enable();
                        else
                            mBluetoothAdapter.disable();
                    }
                } else {
                    if (on)
                        mBluetoothAdapter.disable();
                    else
                        mBluetoothAdapter.enable();
                }
            }

            // BRIGHTNESS

            // ALARM VOLUME
            if (SP.getBoolean(Costants.PREFERENCES_ALARM_SOUND, true)) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (SP.getBoolean(Costants.PREFERENCES_REMEMBER_OLD, true)) {
                    if (on) {
                        SP.edit().putInt(Costants.PREFERENCES_ALARM_SOUND_OLD, am.getStreamVolume(AudioManager.STREAM_ALARM)).apply();
                        am.setStreamVolume(AudioManager.STREAM_ALARM, SP.getInt(Costants.PREFERENCES_ALARM_SOUND_LEVEL, 5), 0);
                    } else {
                        am.setStreamVolume(AudioManager.STREAM_ALARM, SP.getInt(Costants.PREFERENCES_ALARM_SOUND_OLD, 0), 0);
                    }
                } else {
                    if (on)
                        am.setStreamVolume(AudioManager.STREAM_ALARM, SP.getInt(Costants.PREFERENCES_ALARM_SOUND_LEVEL, 5), 0);
                    else
                        am.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
                }
            }

            // DO NOT DISTURB
            if (SP.getBoolean(Costants.PREFERENCES_DO_NOT_DISTURB, true)) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (SP.getBoolean(Costants.PREFERENCES_REMEMBER_OLD, true)) {
                    if (on) {
                        SP.edit().putInt(Costants.PREFERENCES_DO_NOT_DISTURB_OLD, am.getRingerMode()).apply();
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        // TODO
                        /*NotificationListenerService service = new NotificationListenerService() {};
                        service.requestInterruptionFilter(NotificationListenerService.INTERRUPTION_FILTER_PRIORITY);*/
                    } else {
                        am.setRingerMode(SP.getInt(Costants.PREFERENCES_DO_NOT_DISTURB_OLD, AudioManager.RINGER_MODE_NORMAL));
                    }
                } else {
                    if (on)
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    else
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
            }

            SP.edit().putBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, on).apply();
            if (on)
                SP.edit().putLong(Costants.PREFERENCES_START_TIME, Calendar.getInstance().getTimeInMillis()).apply();
            Utils.showNotification(this, on);
            Utils.updateWidget(this);
        }
    }

}
