package com.nego.nightmode.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nego.nightmode.Alarm;
import com.nego.nightmode.Mode;

import java.util.ArrayList;

public class DbAdapter {


    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    // Database fields
    private static final String DATABASE_TABLE_ALARM = "alarm";
    private static final String DATABASE_TABLE_MODE = "mode";

    public static final String KEY_ID = "id";
    public static final String KEY_DAY = "day";
    public static final String KEY_START = "start";
    public static final String KEY_END = "end";

    public static final String KEY_MODE_ID = "id";
    public static final String KEY_MODE_NAME = "name";
    public static final String KEY_MODE_ICON = "icon";
    public static final String KEY_MODE_COLOR = "color";
    public static final String KEY_MODE_DEF = "def";
    public static final String KEY_MODE_IS_DEFAULT = "isDefault";
    public static final String KEY_MODE_NOTIFICATION = "notification";
    public static final String KEY_MODE_NFC = "nfc";
    public static final String KEY_MODE_WIFI = "wifi";
    public static final String KEY_MODE_BLUETOOTH = "bluetooth";
    public static final String KEY_MODE_ALARM_SOUND = "alarm_sound";
    public static final String KEY_MODE_ALARM_LEVEL = "alarm_level";
    public static final String KEY_MODE_DO_NOT_DISTURB = "do_not_disturb";
    public static final String KEY_MODE_PRIORITY_MODE = "priority_mode";
    public static final String KEY_MODE_SCREEN_OFF = "screen_off";
    public static final String KEY_MODE_LAST_ACTIVATION = "last_activation";


    public DbAdapter(Context context) {
        this.context = context;
    }

    public DbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        if (database.getVersion() < DatabaseHelper.DATABASE_VERSION)
            dbHelper.onUpgrade(database, database.getVersion(), DatabaseHelper.DATABASE_VERSION);
        else
            dbHelper.onCreate(database);
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues createContentValues(int day, long start, long end) {
        ContentValues values = new ContentValues();
        values.put(KEY_DAY, day);
        values.put(KEY_START, start);
        values.put(KEY_END, end);

        return values;
    }

    private ContentValues createModeValues(int id, String name, String icon, String color, String def, int isDefault, int notification, String nfc, int wifi, int bluetooth, int alarm_sound, int alarm_level, int do_no_disturb, int priority_mode, int screen_off, long last_activation) {
        ContentValues values = new ContentValues();
        values.put(KEY_MODE_ID, id);
        values.put(KEY_MODE_NAME, name);
        values.put(KEY_MODE_ICON, icon);
        values.put(KEY_MODE_COLOR, color);
        values.put(KEY_MODE_DEF, def);
        values.put(KEY_MODE_IS_DEFAULT, isDefault);
        values.put(KEY_MODE_NOTIFICATION, notification);
        values.put(KEY_MODE_NFC, nfc);
        values.put(KEY_MODE_WIFI, wifi);
        values.put(KEY_MODE_BLUETOOTH, bluetooth);
        values.put(KEY_MODE_ALARM_SOUND, alarm_sound);
        values.put(KEY_MODE_ALARM_LEVEL, alarm_level);
        values.put(KEY_MODE_DO_NOT_DISTURB, do_no_disturb);
        values.put(KEY_MODE_PRIORITY_MODE, priority_mode);
        values.put(KEY_MODE_SCREEN_OFF, screen_off);
        values.put(KEY_MODE_LAST_ACTIVATION, last_activation);

        return values;
    }

    private ContentValues createModeValues(String name, String icon, String color, String def, int isDefault, int notification, String nfc, int wifi, int bluetooth, int alarm_sound, int alarm_level, int do_no_disturb, int priority_mode, int screen_off, long last_activation) {
        ContentValues values = new ContentValues();
        values.put(KEY_MODE_NAME, name);
        values.put(KEY_MODE_ICON, icon);
        values.put(KEY_MODE_COLOR, color);
        values.put(KEY_MODE_DEF, def);
        values.put(KEY_MODE_IS_DEFAULT, isDefault);
        values.put(KEY_MODE_NOTIFICATION, notification);
        values.put(KEY_MODE_NFC, nfc);
        values.put(KEY_MODE_WIFI, wifi);
        values.put(KEY_MODE_BLUETOOTH, bluetooth);
        values.put(KEY_MODE_ALARM_SOUND, alarm_sound);
        values.put(KEY_MODE_ALARM_LEVEL, alarm_level);
        values.put(KEY_MODE_DO_NOT_DISTURB, do_no_disturb);
        values.put(KEY_MODE_PRIORITY_MODE, priority_mode);
        values.put(KEY_MODE_SCREEN_OFF, screen_off);
        values.put(KEY_MODE_LAST_ACTIVATION, last_activation);

        return values;
    }

    public long createAlarm(Alarm a) {
        ContentValues initialValues = createContentValues(a.getDay(), a.getStart(), a.getEnd());
        return database.insertOrThrow(DATABASE_TABLE_ALARM, null, initialValues);
    }

    public boolean deleteAlarm(int day) {
        return database.delete(DATABASE_TABLE_ALARM, KEY_DAY + "==" + day, null) > 0;
    }


    public Cursor fetchAllAlarms() {
        return database.query(DATABASE_TABLE_ALARM, new String[]{KEY_ID, KEY_DAY, KEY_START, KEY_END}, null, null, null, null, null);
    }

    public Cursor getAlarmByDay(int day) {
        Cursor mCursor = database.query(true, DATABASE_TABLE_ALARM, new String[]{
                        KEY_ID, KEY_DAY, KEY_START, KEY_END},
                KEY_DAY + " == " + day, null, null, null, null, null);

        return mCursor;
    }


    // MODES



    public long createMode(Mode m) {
        ContentValues initialValues = createModeValues(m.getName(), m.getIcon(), m.getColor(), m.getDef(), m.getIsDefaultDB(), m.getNotificationDB(), m.getNfc(), m.getWifiDB(), m.getBluetoothDB(), m.getAlarm_soundDB(), m.getAlarm_level(), m.getDo_no_disturbDB(), m.getPriority_modeDB(), m.getScreen_offDB(), m.getLast_activation());
        return database.insertOrThrow(DATABASE_TABLE_MODE, null, initialValues);
    }

    public long createDefaultMode(Mode m) {
        ContentValues initialValues = createModeValues(m.getId(), m.getName(), m.getIcon(), m.getColor(), m.getDef(), m.getIsDefaultDB(), m.getNotificationDB(), m.getNfc(), m.getWifiDB(), m.getBluetoothDB(), m.getAlarm_soundDB(), m.getAlarm_level(), m.getDo_no_disturbDB(), m.getPriority_modeDB(), m.getScreen_offDB(), m.getLast_activation());
        return database.insertOrThrow(DATABASE_TABLE_MODE, null, initialValues);
    }

    public boolean updateMode(Mode m) {
        ContentValues updateValues = createModeValues(m.getId(), m.getName(), m.getIcon(), m.getColor(), m.getDef(), m.getIsDefaultDB(), m.getNotificationDB(), m.getNfc(), m.getWifiDB(), m.getBluetoothDB(), m.getAlarm_soundDB(), m.getAlarm_level(), m.getDo_no_disturbDB(), m.getPriority_modeDB(), m.getScreen_offDB(), m.getLast_activation());
        return database.update(DATABASE_TABLE_MODE, updateValues, KEY_MODE_ID + "==" + m.getId(), null) > 0;
    }

    public boolean deleteMode(int ID) {
        return database.delete(DATABASE_TABLE_MODE, KEY_MODE_ID + "==" + ID, null) > 0;
    }

    public Cursor fetchAllModes() {
        return database.query(DATABASE_TABLE_MODE, new String[]{KEY_MODE_ID, KEY_MODE_NAME, KEY_MODE_ICON, KEY_MODE_COLOR, KEY_MODE_DEF, KEY_MODE_IS_DEFAULT, KEY_MODE_NOTIFICATION, KEY_MODE_NFC, KEY_MODE_WIFI, KEY_MODE_BLUETOOTH, KEY_MODE_ALARM_SOUND, KEY_MODE_ALARM_LEVEL, KEY_MODE_DO_NOT_DISTURB, KEY_MODE_PRIORITY_MODE, KEY_MODE_SCREEN_OFF, KEY_MODE_LAST_ACTIVATION},null, null, null, null, null);
    }

    public Cursor getModeById(int id) {
        return database.query(true, DATABASE_TABLE_MODE, new String[]{
                        KEY_MODE_ID, KEY_MODE_NAME, KEY_MODE_ICON, KEY_MODE_COLOR, KEY_MODE_DEF, KEY_MODE_IS_DEFAULT, KEY_MODE_NOTIFICATION, KEY_MODE_NFC, KEY_MODE_WIFI, KEY_MODE_BLUETOOTH, KEY_MODE_ALARM_SOUND, KEY_MODE_ALARM_LEVEL, KEY_MODE_DO_NOT_DISTURB, KEY_MODE_PRIORITY_MODE, KEY_MODE_SCREEN_OFF, KEY_MODE_LAST_ACTIVATION},
                KEY_MODE_ID + " == '" + id + "'", null, null, null, null, null);
    }

    public Cursor getModeByName(String name) {
        return database.query(true, DATABASE_TABLE_MODE, new String[]{
                        KEY_MODE_ID, KEY_MODE_NAME, KEY_MODE_ICON, KEY_MODE_COLOR, KEY_MODE_DEF, KEY_MODE_IS_DEFAULT, KEY_MODE_NOTIFICATION, KEY_MODE_NFC, KEY_MODE_WIFI, KEY_MODE_BLUETOOTH, KEY_MODE_ALARM_SOUND, KEY_MODE_ALARM_LEVEL, KEY_MODE_DO_NOT_DISTURB, KEY_MODE_PRIORITY_MODE, KEY_MODE_SCREEN_OFF, KEY_MODE_LAST_ACTIVATION},
                KEY_MODE_NAME + " == '" + name + "'", null, null, null, null, null);
    }

    public boolean deleteDirtyData() {
        return database.delete(DATABASE_TABLE_MODE, null, null) > 0;
    }


}