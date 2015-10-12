package com.nego.nightmode.Functions;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.nego.nightmode.Costants;
import com.nego.nightmode.R;
import com.nego.nightmode.Receiver.StatusChanged;


public class NotificationF {

    public static void NotificationAdd(Context context) {
        Intent i=new Intent(context, StatusChanged.class);
        i.setAction(Costants.ACTION_NIGHT_MODE_OFF);
        PendingIntent pi= PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder n  = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name_enabled))
                .setContentText(context.getString(R.string.action_disabled_night_mode))
                .setSmallIcon(R.drawable.ic_not_night_mode)
                .setContentIntent(pi)
                .setOngoing(true)
                .setPriority(-1)
                .setColor(ContextCompat.getColor(context, R.color.primary))
                .setPriority(Notification.PRIORITY_MIN)
                .setAutoCancel(false);


        notificationManager.notify(0, n.build());
    }

    public static void CancelAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

}
