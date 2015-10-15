package com.nego.nightmode.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.nego.nightmode.Costants;
import com.nego.nightmode.R;
import com.nego.nightmode.Receiver.StatusChanged;

public class NightModeToggle extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int i = 0; i < appWidgetIds.length; i++) {

            SharedPreferences SP = context.getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.night_mode_toggle);
            rv.setImageViewResource(R.id.button, SP.getBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, false) ? R.mipmap.sun : R.mipmap.ic_launcher);

            Intent change_i = new Intent(context, StatusChanged.class);
            if (SP.getBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, false))
                change_i.setAction(Costants.ACTION_NIGHT_MODE_OFF);
            else
                change_i.setAction(Costants.ACTION_NIGHT_MODE_ON);
            PendingIntent pi= PendingIntent.getBroadcast(context, 0, change_i, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.button, pi);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            ComponentName thisWidget = new ComponentName(context, NightModeToggle.class);
            int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisWidget);
            if (appWidgetIds.length > 0)
                onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        }
    }

}

