package com.nego.nightmode;

import android.content.Context;
import android.os.Build;

import com.nego.nightmode.Functions.NotificationF;


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
        if (activated) {
            NotificationF.NotificationAdd(context);
        } else {
            NotificationF.CancelAllNotification(context);
        }
    }

}
