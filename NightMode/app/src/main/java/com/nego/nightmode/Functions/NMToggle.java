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
import com.nego.nightmode.R;
import com.nego.nightmode.Receiver.DeviceAdminReceiver;
import com.nego.nightmode.Utils;

import java.util.Calendar;

public class NMToggle extends IntentService {

    public static void startAction(Context context, String action) {
        Intent intent = new Intent(context, NMToggle.class);
        intent.setAction(action);
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
                        //TODO priorityMode();
                    } else {
                        am.setRingerMode(SP.getInt(Costants.PREFERENCES_DO_NOT_DISTURB_OLD, AudioManager.RINGER_MODE_NORMAL));
                    }
                } else {
                    if (on) {
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        //TODO priorityMode();
                    } else {
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }
                }
            }

            SP.edit().putBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, on).apply();
            if (on)
                SP.edit().putLong(Costants.PREFERENCES_START_TIME, Calendar.getInstance().getTimeInMillis()).apply();
            Utils.showNotification(this, on);
            Utils.updateWidget(this);
            sendResponse(Costants.ACTION_NIGHT_MODE_TOGGLE);

            // SCREEN OFF
            if (SP.getBoolean(Costants.PREFERENCES_SCREEN_OFF, false) && on) {
                final DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName adminReceiver = new ComponentName(NMToggle.this, DeviceAdminReceiver.class);
                final boolean admin = policyManager.isAdminActive(adminReceiver);
                if (admin) {
                    Log.i("SCREEN OFF", "Going to sleep now.");
                    policyManager.lockNow();
                } else {
                    Log.i("SCREEN OFF", "Not an admin");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        stop();
    }

    public boolean haveNotificationAccess() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();

        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName))
            return false;
        else
            return true;
    }

    private NLService nlService = null;
    private boolean mBound = false;

    public void priorityMode() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && haveNotificationAccess()){
            start();
        }
    }

    public void start() {
        Intent intent = new Intent(this, NLService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void stop() {
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
            Log.i("PRIORITY", "BIND STOP BY FINISH");
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            NLService.LocalBinder binder = (NLService.LocalBinder) service;
            nlService = binder.getService();
            mBound = true;
            Log.i("PRIORITY", "BIND START");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                nlService.requestInterruptionFilter(NotificationListenerService.INTERRUPTION_FILTER_PRIORITY);
            Log.i("PRIORITY", "FATTO");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Log.i("PRIORITY", "BIND STOP BY CONNECTION");
        }
    };


}
