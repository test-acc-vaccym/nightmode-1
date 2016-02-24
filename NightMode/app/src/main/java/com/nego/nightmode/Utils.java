package com.nego.nightmode;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import com.nego.nightmode.Functions.NotificationF;
import com.nego.nightmode.Widget.NightModeToggle;
import com.nego.nightmode.database.DbAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Utils {



    public static void updateWidget(Context context) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] widgetIds = widgetManager.getAppWidgetIds(new ComponentName(context, NightModeToggle.class));
        Intent update = new Intent(context, NightModeToggle.class);
        update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.button);
        context.sendBroadcast(update);
    }


    public static boolean isBrokenSamsungDevice() {
        return (Build.MANUFACTURER.equalsIgnoreCase("samsung")
                && isBetweenAndroidVersions(
                Build.VERSION_CODES.LOLLIPOP,
                Build.VERSION_CODES.LOLLIPOP_MR1));
    }

    public static boolean isBetweenAndroidVersions(int min, int max) {
        return Build.VERSION.SDK_INT >= min && Build.VERSION.SDK_INT <= max;
    }

    public static void showNotification(Context context, Mode m) {
        if (m.getNotification()) {
            NotificationF.NotificationAdd(context, m);
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
                return context.getString(R.string.text_at) + " " + HM.format(new Date(byR.getTimeInMillis()));
            } else if (today.get(Calendar.YEAR) == byR.get(Calendar.YEAR)) {
                return context.getString(R.string.text_on) + " " + DM.format(new Date(byR.getTimeInMillis()));
            } else {
                return context.getString(R.string.text_on) + " " + MY.format(new Date(byR.getTimeInMillis()));
            }
        }
    }


    public static String getHour(Context context, long date) {
        if (date == 0) {
            return context.getString(R.string.text_never);
        } else {
            Calendar today = Calendar.getInstance();
            Calendar byR = Calendar.getInstance();
            byR.setTimeInMillis(date);
            SimpleDateFormat HM = new SimpleDateFormat("HH:mm");
            return HM.format(new Date(byR.getTimeInMillis()));
        }
    }


    public static String ByteArrayToHexString(byte [] inarray)
    {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    public static boolean updateToMode(Context context, SharedPreferences SP) {
        if (!SP.getBoolean(Costants.UPDATED_TO_MODE, false)) {
            // Apro il db così da creare la tabella mode
            DbAdapter dbHelper = new DbAdapter(context);
            dbHelper.open();
            // Ripulisco eventuali dati sporchi
            dbHelper.deleteDirtyData();
            // Inserisco i modi principali
            // SUN
            Mode sun = new Mode(1, Costants.DEFAULT_MODE_DAY, Costants.DEFAULT_MODE_DAY, Costants.DEFAULT_MODE_DAY, Costants.DEFAULT_MODE_DAY, 1, 0, "", 1, 0, 0, 0, 0, AudioManager.RINGER_MODE_NORMAL, 0, 0);
            if (sun.insertDefault(dbHelper)) {
                // NIGHT
                Mode night = new Mode(2, Costants.DEFAULT_MODE_NIGHT, Costants.DEFAULT_MODE_NIGHT, Costants.DEFAULT_MODE_NIGHT, Costants.DEFAULT_MODE_NIGHT, 1, SP.getBoolean(Costants.PREFERENCES_NOTIFICATION, true) ? 1 : 0, SP.getString(Costants.PREFERENCES_NFC_ID, ""), SP.getBoolean(Costants.PREFERENCES_WIFI, true) ? 1 : 0, SP.getBoolean(Costants.PREFERENCES_BLUETOOTH, true) ? 1 : 0, SP.getBoolean(Costants.PREFERENCES_ALARM_SOUND, true) ? 1 : 0, SP.getInt(Costants.PREFERENCES_ALARM_SOUND_LEVEL, 5), SP.getBoolean(Costants.PREFERENCES_DO_NOT_DISTURB, true) ? 1 : 0, SP.getInt(Costants.PREFERENCES_DO_NOT_DISTURB_OLD, AudioManager.RINGER_MODE_NORMAL), SP.getBoolean(Costants.PREFERENCES_SCREEN_OFF, false) ? 1 : 0, SP.getLong(Costants.PREFERENCES_START_TIME, 0));
                if (!night.insertDefault(dbHelper))
                    return false;
            } else
                return false;
            dbHelper.close();
            SP.edit().putBoolean(Costants.UPDATED_TO_MODE, true).apply();
        }
        return true;
    }

    public static String getModeName(Context context, String name) {
        switch (name) {
            case Costants.DEFAULT_MODE_DAY:
                return "Day";
            case Costants.DEFAULT_MODE_NIGHT:
                return context.getString(R.string.app_name);
            default:
                return name;
        }
    }

    public static int getModeIcon(String name) {
        switch (name) {
            case Costants.DEFAULT_MODE_DAY:
                return R.drawable.sun;
            case Costants.DEFAULT_MODE_NIGHT:
                return R.drawable.ic_action_night;
            default:
                return 0;
        }
    }

    public static int getModeColor(String name) {
        switch (name) {
            case Costants.DEFAULT_MODE_DAY:
                return R.color.primary;
            case Costants.DEFAULT_MODE_NIGHT:
                return R.color.primary_dark;
            default:
                return 0;
        }
    }

    public static Mode getActualMode(Context context) {
        Mode actual = null;
        SharedPreferences SP = context.getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
        DbAdapter dbHelper = new DbAdapter(context);
        dbHelper.open();

        int n = SP.getInt(Costants.ACTUAL_MODE, 0);
        Cursor c;
        if (n == 0) {
            c = dbHelper.getModeByName(Costants.DEFAULT_MODE_DAY);
        } else {
            c = dbHelper.getModeById(n);
        }
        if (c.moveToFirst())
            actual = new Mode(c);
        c.close();

        dbHelper.close();
        return actual;
    }
}
