package com.nego.nightmode;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.nego.nightmode.Functions.NotificationF;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Utils {

/*

    public static void updateWidget(Context context) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] widgetIds = widgetManager.getAppWidgetIds(new ComponentName(context, FocusWidget.class));
        Intent update = new Intent(context, FocusWidget.class);
        update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.list);
        context.sendBroadcast(update);
    }*/


    public static boolean isBrokenSamsungDevice() {
        return (Build.MANUFACTURER.equalsIgnoreCase("samsung")
                && isBetweenAndroidVersions(
                Build.VERSION_CODES.LOLLIPOP,
                Build.VERSION_CODES.LOLLIPOP_MR1));
    }

    public static boolean isBetweenAndroidVersions(int min, int max) {
        return Build.VERSION.SDK_INT >= min && Build.VERSION.SDK_INT <= max;
    }
/*
    public static void tab_intro(final Context context) {
        SharedPreferences SP = context.getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
        if (!SP.getBoolean(Costants.PREFERENCE_INTRO, false)) {
            SharedPreferences.Editor editor = SP.edit();
            editor.putBoolean(Costants.PREFERENCE_INTRO, true);
            editor.apply();
            ((Main) context).findViewById(R.id.first_use).setVisibility(View.VISIBLE);
            ((Main) context).findViewById(R.id.button_first_use).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Main) context).findViewById(R.id.first_use).setVisibility(View.GONE);
                }
            });
            context.startActivity(new Intent(context, Intro.class));
        }
    }*/

    public static void showNotification(Context context, boolean activated) {
        SharedPreferences SP = context.getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
        if (activated && SP.getBoolean(Costants.PREFERENCES_NOTIFICATION, true)) {
            NotificationF.NotificationAdd(context);
        } else {
            NotificationF.CancelAllNotification(context);
        }
    }


    public static String getDate(Context context, long date) {
        if (date == 0) {
            return context.getString(R.string.text_never);
        } else {
            Calendar today = Calendar.getInstance();
            Calendar byR = Calendar.getInstance();
            byR.setTimeInMillis(date);
            SimpleDateFormat HM = new SimpleDateFormat("HH:mm");
            SimpleDateFormat DM = new SimpleDateFormat("MMM d, HH:mm");
            SimpleDateFormat MY = new SimpleDateFormat("MMM d y, HH:mm ");
            if (today.get(Calendar.YEAR) == byR.get(Calendar.YEAR) &&
                    today.get(Calendar.MONTH) == byR.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) == byR.get(Calendar.DAY_OF_MONTH)) {
                return HM.format(new Date(byR.getTimeInMillis()));
            } else if (today.get(Calendar.YEAR) == byR.get(Calendar.YEAR)) {
                return DM.format(new Date(byR.getTimeInMillis()));
            } else {
                return MY.format(new Date(byR.getTimeInMillis()));
            }
        }
    }

}
