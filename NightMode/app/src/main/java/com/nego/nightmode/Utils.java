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
        if (SP.getInt(Costants.UPDATED_TO_MODE, 0) == 0) {
            // Apro il db così da creare la tabella mode
            DbAdapter dbHelper = new DbAdapter(context);
            dbHelper.open();
            // Ripulisco eventuali dati sporchi
            dbHelper.deleteDirtyData();
            // Inserisco i modi principali
            // SUN
            Mode sun = new Mode(1, // ID
                    Costants.DEFAULT_MODE_DAY, // NOME
                    Costants.DEFAULT_MODE_DAY, // ICON
                    Costants.DEFAULT_MODE_DAY, // COLOR
                    Costants.DEFAULT_MODE_DAY, // DEF
                    1, // DEFAULT
                    0, // NOTIFICATION
                    "", // NFC
                    1, // WIFI
                    0, // BLUETOOTH
                    0, // ALARM_SOUND
                    0, // ALARM_LEVEL
                    0, // DO NOT DISTURB
                    0, // PRIORITY_MODE
                    0, // SCREEN_OFF
                    Calendar.getInstance().getTimeInMillis()); // LAST ACTIVATION
            if (sun.insertDefault(dbHelper)) {
                // NIGHT
                Mode night = new Mode(2,
                        Costants.DEFAULT_MODE_NIGHT,
                        Costants.DEFAULT_MODE_NIGHT,
                        Costants.DEFAULT_MODE_NIGHT,
                        Costants.DEFAULT_MODE_NIGHT,
                        1,
                        SP.getBoolean(Costants.PREFERENCES_NOTIFICATION, true) ? 1 : 0,
                        SP.getString(Costants.PREFERENCES_NFC_ID, ""),
                        SP.getBoolean(Costants.PREFERENCES_WIFI, true) ? 1 : 0,
                        SP.getBoolean(Costants.PREFERENCES_BLUETOOTH, true) ? 1 : 0,
                        SP.getBoolean(Costants.PREFERENCES_ALARM_SOUND, true) ? 1 : 0,
                        SP.getInt(Costants.PREFERENCES_ALARM_SOUND_LEVEL, 5),
                        SP.getBoolean(Costants.PREFERENCES_DO_NOT_DISTURB, true) ? 1 : 0,
                        SP.getBoolean(Costants.PREFERENCES_PRIORITY_MODE, true) ? 1 : 0,
                        SP.getBoolean(Costants.PREFERENCES_SCREEN_OFF, false) ? 1 : 0,
                        SP.getLong(Costants.PREFERENCES_START_TIME, 0));
                if (!night.insertDefault(dbHelper))
                    return false;
            } else
                return false;
            dbHelper.close();

            // NEXT CYCLE
            if (SP.edit().putInt(Costants.UPDATED_TO_MODE, 1).commit())
                updateToMode(context, SP);
            else
                return false;
        }
        return true;
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

    public static Mode getDayMode(Context context) {
        Mode day = null;
        DbAdapter dbHelper = new DbAdapter(context);
        dbHelper.open();
        Cursor c = dbHelper.getModeByName(Costants.DEFAULT_MODE_DAY);
        if (c.moveToFirst())
            day = new Mode(c);
        c.close();
        dbHelper.close();
        return day;
    }


    public static String getModeLastActivation (Context context, long date) {
        if (date == 0) {
            return context.getString(R.string.text_never);
        } else {
            long difference = (Calendar.getInstance().getTimeInMillis() - date) / 1000;
            if (difference < 60) { // Secondi
                return difference + context.getString(R.string.seconds);
            } else if (difference >= 60 && difference < 3600) { // Minuti
                return difference / 60 + context.getString(R.string.minutes);
            } else if (difference >= (60 * 60) && difference < (60*60*24)) { // Ore
                return difference / (60 * 60) + context.getString(R.string.hours);
            } else if (difference >= (60*60*24) && difference < (60*60*24*7)) { // Giorni
                return difference / (60*60*24) + context.getString(R.string.days);
            } else { // Settimane
                return difference / (60*60*24*7) + context.getString(R.string.weeks);
            }
        }
    }
}
