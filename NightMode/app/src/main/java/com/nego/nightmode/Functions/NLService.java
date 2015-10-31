package com.nego.nightmode.Functions;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.nego.nightmode.Costants;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class NLService extends NotificationListenerService {

    private NLServiceReceiver nlservicereciver;

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Costants.ACTION_NOTIFICATION_LISTENER_SERVICE);
        registerReceiver(nlservicereciver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Intent i = new  Intent(Costants.ACTION_NOTIFICATION_LISTENER_SERVICE);
        i.putExtra(Costants.NOTIFICATION_PACKAGE, sbn.getPackageName());
        sendBroadcast(i);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Costants.ACTION_NOTIFICATION_LISTENER_SERVICE)) {
                SharedPreferences SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
                if (getPackageName().equals(intent.getStringExtra(Costants.NOTIFICATION_PACKAGE)) && SP.getBoolean(Costants.PREFERENCES_PRIORITY_MODE, true)) {
                    requestInterruptionFilter(INTERRUPTION_FILTER_PRIORITY);
                }
            }
        }
    }


}